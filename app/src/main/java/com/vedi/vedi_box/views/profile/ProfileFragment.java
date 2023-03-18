package com.vedi.vedi_box.views.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vedi.vedi_box.R;
import com.vedi.vedi_box.databinding.ProfileFragmentBinding;
import com.vedi.vedi_box.listeners.BackPressListener;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.UserModel;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.views.delivery.Delivery;
import com.vedi.vedi_box.views.shop.ShopFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProfileFragment extends Fragment implements BackPressListener {
    private static final String TAG = "ProfileFragment";


    ProfileFragmentBinding binding;
    private PreferManager preferManager;
    private ProfileViewModel profileViewModel;
    private String UserID, UserMobile;


    public static BackPressListener listener;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ProfileFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        if (activity != null) {
            preferManager = new PreferManager(activity);
            UserID = preferManager.getString(Constants.KEY_USER_ID);
            UserMobile = preferManager.getString(Constants.KEY_USER_MOBILE);

            profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

            getProfileDetails();

            binding.profileEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, Delivery.class);
                    intent.putExtra("Class", "Profile");
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });
        }

    }

    private void getProfileDetails() {
        profileViewModel.getUserProfileDetails(UserID).observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> userModels) {
                for (UserModel user : userModels) {

                    preferManager.putString(Constants.KEY_USER_FULL_NAME, user.getUser_name());

                    if (user.getUser_name().equals("") && user.getUser_email().equals("") && user.getHouse_or_flat().equals("") && user.getArea_street().equals("") && user.getState().equals("") && user.getTaluk().equals("") && user.getPincode().equals("") && user.getCity().equals("")) {
                        UserModel userModel = new UserModel(UserMobile, "FullName", "Email", "Address");
                        binding.setUser(userModel);
                    } else if (user.getUser_name().equals("") && user.getUser_email().equals("") && !user.getHouse_or_flat().equals("") && !user.getArea_street().equals("") && !user.getState().equals("") && !user.getTaluk().equals("") && !user.getPincode().equals("") && !user.getCity().equals("")) {
                        UserModel userModel = new UserModel(UserMobile, "FullName", "Email", user.getHouse_or_flat() + ", " + user.getArea_street() + ", " + user.getTaluk() + ", " + user.getCity() + "," + user.getPincode() + ", \n" + user.getState() + ".");
                        binding.setUser(userModel);
                    } else if (!user.getUser_name().equals("") && user.getUser_email().equals("") && !user.getHouse_or_flat().equals("") && !user.getArea_street().equals("") && !user.getState().equals("") && !user.getTaluk().equals("") && !user.getPincode().equals("") && !user.getCity().equals("")) {
                        UserModel userModel = new UserModel(UserMobile, user.getUser_name(), "Email", user.getHouse_or_flat() + ", " + user.getArea_street() + ", " + user.getTaluk() + ", " + user.getCity() + "," + user.getPincode() + ", \n" + user.getState() + ".");
                        binding.setUser(userModel);
                    } else {
                        UserModel userModel = new UserModel(UserMobile, user.getUser_name(), user.getUser_email(), user.getHouse_or_flat() + ", " + user.getArea_street() + ", " + user.getTaluk() + ", " + user.getCity() + "," + user.getPincode() + ", \n" + user.getState() + ".");
                        binding.setUser(userModel);
                    }

                }
            }
        });
        profileViewModel.getOrderCount(UserID).observe(getViewLifecycleOwner(), new Observer<List<APIResponse>>() {
            @Override
            public void onChanged(List<APIResponse> apiResponses) {
                for (APIResponse apiResponse : apiResponses) {
                    if (apiResponse.getPurchase_amount() != null) {
                        binding.profileAmountSpend.setText("₹ " + apiResponse.getPurchase_amount());
                    } else {

                        binding.profileAmountSpend.setText("₹ 0");
                    }
                    binding.profileTotalOrderCount.setText(apiResponse.getTotal_orders());

                }

            }
        });
    }

    @Override
    public void onPause() {
        listener = null;
        super.onPause();
        getProfileDetails();
    }

    @Override
    public void onResume() {
        super.onResume();
        listener = this;
        getProfileDetails();
    }

    @Override
    public void onBackPressed() {
        listener = null;
        requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.fragment_container, new ShopFragment()).addToBackStack(null).commit();
    }
}