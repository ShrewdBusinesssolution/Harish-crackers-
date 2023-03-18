package com.vedi.vedi_box.views.notification;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vedi.vedi_box.R;
import com.vedi.vedi_box.adapters.NotificationListAdapter;
import com.vedi.vedi_box.databinding.NotificationFragmentBinding;
import com.vedi.vedi_box.listeners.BackPressListener;
import com.vedi.vedi_box.models.Notify;
import com.vedi.vedi_box.views.shop.ShopFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotificationFragment extends Fragment implements BackPressListener {

    private static final String TAG = "NotificationFragment";

    NotificationFragmentBinding binding;
    private MediaPlayer mp;
    private NotificationViewModel mViewModel;
    private NotificationListAdapter notificationListAdapter;


    public static BackPressListener listener;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NotificationFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            mp = MediaPlayer.create(requireActivity(), R.raw.single_shot);
//            notificationFragmentBinding.notificationsBack.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mp.start();
//                    requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.fragment_container, new ShopFragment()).commit();
//                }
//            });
            binding.notificationRecycler.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
            binding.notificationRecycler.setHasFixedSize(true);

            mViewModel = new ViewModelProvider(requireActivity()).get(NotificationViewModel.class);

            mViewModel.notifyRepo.getNotifications().observe(getViewLifecycleOwner(), new Observer<List<Notify>>() {
                @Override
                public void onChanged(List<Notify> notifies) {

                    if (notifies.size() > 0) {
                        binding.notificationRecycler.setVisibility(View.VISIBLE);
                        binding.notificationAnimation.cancelAnimation();
                        binding.emptyLayout.setVisibility(View.GONE);
                    } else {
                        binding.notificationRecycler.setVisibility(View.GONE);
                        binding.emptyLayout.setVisibility(View.VISIBLE);
                        binding.notificationAnimation.playAnimation();
                    }

                    notificationListAdapter = new NotificationListAdapter(requireActivity(), notifies);
                    notificationListAdapter.notifyDataSetChanged();
                    binding.notificationRecycler.setAdapter(notificationListAdapter);
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