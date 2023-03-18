package com.vedi.vedi_box.views.track_order;

import android.app.Activity;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vedi.vedi_box.R;
import com.vedi.vedi_box.adapters.TrackListAdapter;
import com.vedi.vedi_box.databinding.ActivityTrackBinding;
import com.vedi.vedi_box.databinding.PendingAlertBinding;
import com.vedi.vedi_box.listeners.BackPressListener;
import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.views.invoice.InvoiceFragment;
import com.vedi.vedi_box.views.order.OrdersFragment;
import com.vedi.vedi_box.views.shop.ShopFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrackFragment extends Fragment implements BackPressListener {

    private static final String TAG = "TrackFragment";

    ActivityTrackBinding binding;
    private PreferManager preferManager;
    private MediaPlayer mp;
    private TrackListAdapter trackListAdapter;
    private TrackViewModel trackViewModel;
    private Dialog dialogView;
    private String userID;
    private String layoutTransaction = "Nothing";

    public static BackPressListener listener;

    public static TrackFragment newInstance() {
        return new TrackFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ActivityTrackBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            preferManager = new PreferManager(activity);
            mp = MediaPlayer.create(requireActivity(), R.raw.single_shot);
            dialogView = new Dialog(activity, R.style.FullHeightDialog);
            dialogView.setCancelable(false);
            userID = preferManager.getString(Constants.KEY_USER_ID);


            trackViewModel = new ViewModelProvider(requireActivity()).get(TrackViewModel.class);

            binding.trackDetailsBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    onBackPressed();
                }
            });


            binding.trackOrderRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
            binding.trackOrderRecyclerView.setHasFixedSize(true);

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                String orderID = bundle.getString(Constants.KEY_PRODUCT_ID);
                String OrderStatus = bundle.getString(Constants.KEY_ORDER_STATUS);

                binding.trackDetailsCommon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mp.start();
                        if (OrderStatus != null && !OrderStatus.equals("Pending")) {
                            requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragment_container, new InvoiceFragment()).commit();
                        } else {
                            getPendingAlert();
                        }
                    }
                });

                preferManager.putString(Constants.KEY_PRODUCT_ID, orderID);

                trackViewModel.getTrackList(activity, userID, orderID).observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {

                        trackListAdapter = new TrackListAdapter(activity, products);
                        trackListAdapter.notifyDataSetChanged();
                        binding.trackOrderRecyclerView.setAdapter(trackListAdapter);

                    }
                });

            } else {
                String orderID = preferManager.getString(Constants.KEY_PRODUCT_ID);

                trackViewModel.getTrackList(activity, userID, orderID).observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {

                        trackListAdapter = new TrackListAdapter(activity, products);
                        trackListAdapter.notifyDataSetChanged();
                        binding.trackOrderRecyclerView.setAdapter(trackListAdapter);

                    }
                });
            }


            if (binding.trackDetailsCommonTitle.getText().toString().equals("TrackFragment Order")) {
                binding.trackDetailsCommon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mp.start();
                        layoutTransaction = "Something";
                        binding.trackDetailsCommonTitle.setText("Go Home");
                        binding.mainOrderLayout.setVisibility(View.GONE);
                        binding.mainTrackLayout.setVisibility(View.VISIBLE);
                        getHomeClick();
                    }
                });
            }

        }
    }

    private void getHomeClick() {
        if (binding.trackDetailsCommonTitle.getText().toString().equals("Go Home")) {
            binding.trackDetailsCommon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragment_container, new ShopFragment()).commit();

                }
            });
        } else if (binding.trackDetailsCommonTitle.getText().toString().equals("TrackFragment Order")) {
            binding.trackDetailsCommon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    layoutTransaction = "Something";
                    binding.trackDetailsCommonTitle.setText("Go Home");
                    binding.mainOrderLayout.setVisibility(View.GONE);
                    binding.mainTrackLayout.setVisibility(View.VISIBLE);
                    getHomeClick();
                }
            });
        }
    }

    private void getPendingAlert() {

        PendingAlertBinding alertBinding = PendingAlertBinding.inflate(LayoutInflater.from(requireActivity()));
        dialogView.setContentView(alertBinding.getRoot());


        if (dialogView.getWindow() != null) {
            dialogView.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialogView.getWindow().setGravity(Gravity.CENTER);

        }

        alertBinding.pendingAlertClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();

                preferManager.putBoolean(Constants.KEY_ALERT_STATUS, false);

            }
        });
        alertBinding.pendingUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();

                preferManager.putBoolean(Constants.KEY_ALERT_STATUS, false);

                requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.fragment_container, new OrdersFragment()).commit();

            }
        });

        dialogView.show();


    }

    @Override
    public void onPause() {
        listener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        listener = this;
    }

    @Override
    public void onBackPressed() {
        if (layoutTransaction.equals("Nothing")) {
            listener = null;
            if (dialogView != null && dialogView.isShowing()) {
                dialogView.show();
            }
            requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.fragment_container, new OrdersFragment()).commit();
        } else {
            listener = this;
            layoutTransaction = "Nothing";
            binding.trackDetailsCommonTitle.setText("TrackFragment Order");
            getHomeClick();
            binding.mainTrackLayout.setVisibility(View.GONE);
            binding.mainOrderLayout.setVisibility(View.VISIBLE);
        }

    }
}