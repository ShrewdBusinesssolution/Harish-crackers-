package com.vedi.vedi_box.views.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vedi.vedi_box.R;
import com.vedi.vedi_box.adapters.OrderListAdapter;
import com.vedi.vedi_box.databinding.OrdersFragmentBinding;
import com.vedi.vedi_box.listeners.BackPressListener;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.Order;
import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.utilities.RetrofitClient;
import com.vedi.vedi_box.views.shop.ShopFragment;
import com.vedi.vedi_box.views.shop.ShopViewModel;
import com.vedi.vedi_box.views.track_order.TrackFragment;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment implements BackPressListener {

    private static final String TAG = "OrdersFragment";
    private static final int IMG_REQUEST = 121;


    OrdersFragmentBinding binding;
    private PreferManager preferManager;
    private MediaPlayer mp;
    private OrdersViewModel ordersViewModel;
    private ShopViewModel shopViewModel;
    private OrderListAdapter orderListAdapter;
    private String UserID;
    private Uri path;
    private Bitmap bitmap;
    private String Order_Id;

    public static BackPressListener listener;

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = OrdersFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            preferManager = new PreferManager(activity);
            mp = MediaPlayer.create(requireActivity(), R.raw.single_shot);

            UserID = preferManager.getString(Constants.KEY_USER_ID);

            ordersViewModel = new ViewModelProvider(requireActivity()).get(OrdersViewModel.class);
            shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);

            binding.orderRecycler.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
            binding.orderRecycler.setHasFixedSize(true);

            getOrder(activity);

        }

    }

    private void getOrder(Activity activity) {
        ordersViewModel.getOrders(UserID, activity).observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {

                Log.d(TAG, "ORDER-SIZE::::" + orders.size());

                if (orders.size() > 0) {
                    binding.orderRecycler.setVisibility(View.VISIBLE);
                    binding.orderAnimation.cancelAnimation();
                    binding.emptyLayout.setVisibility(View.GONE);
                } else {
                    binding.orderRecycler.setVisibility(View.GONE);
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.orderAnimation.playAnimation();
                }


                List<Product> productList = new ArrayList<>();
                for (Order order : orders) {
                    for (Product product : order.getItems()) {
                        productList.add(new Product(product.getId(), product.getOrder_id(), product.getProduct_name(), product.getProduct_quantity(), product.getProduct_imageurl()));
                    }
                    orderListAdapter = new OrderListAdapter(requireActivity(), orders, productList, new OrderListAdapter.OrderInterface() {
                        @Override
                        public void onItemClick(Order order) {
                            mp.start();

                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.KEY_PRODUCT_ID, order.getOrder_id());
                            bundle.putString(Constants.KEY_ORDER_STATUS, order.getOrder_status());
                            bundle.putString(Constants.KEY_USER_ID, UserID);

                            TrackFragment trackFragment = new TrackFragment();
                            trackFragment.setArguments(bundle);
                            requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragment_container, trackFragment).commit();
                        }

                        @Override
                        public void ImageUpload(Order order) {

                            Order_Id = order.getOrder_id();

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, IMG_REQUEST);

                        }
                    });
                    orderListAdapter.notifyDataSetChanged();
                    binding.orderRecycler.setAdapter(orderListAdapter);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {

            path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);

                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        Activity activity = getActivity();
        if (activity != null) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            byte[] imageInByte = byteArrayOutputStream.toByteArray();

            String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);

            Log.d("TAG", "ImageURL:::" + Order_Id + "\n" + encodedImage);

            Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().ProofUpload(encodedImage, Order_Id);
            call.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), "Proof Uploaded", Toast.LENGTH_SHORT).show();
                        getOrder(activity);
                    } else if (response.code() == 500) {
                        Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });
        }
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
        listener = null;
        requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.fragment_container, new ShopFragment()).addToBackStack(null).commit();
    }
}