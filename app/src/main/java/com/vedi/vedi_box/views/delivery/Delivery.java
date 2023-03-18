package com.vedi.vedi_box.views.delivery;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vedi.vedi_box.R;
import com.vedi.vedi_box.databinding.ActivityDeliveryBinding;
import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.UserModel;
import com.vedi.vedi_box.utilities.Constants;
import com.vedi.vedi_box.utilities.PreferManager;
import com.vedi.vedi_box.utilities.RetrofitClient;
import com.vedi.vedi_box.views.payment.Payment;
import com.vedi.vedi_box.views.profile.ProfileViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Delivery extends AppCompatActivity {

    private static final String TAG = "Delivery";

    ActivityDeliveryBinding binding;
    private DeliveryViewModel deliveryViewModel;
    private ProfileViewModel profileViewModel;
    private PreferManager preferManager;
    private MediaPlayer mp;
    private String UserID,UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mp = MediaPlayer.create(this, R.raw.single_shot);
        preferManager = new PreferManager(this);
        UserID = preferManager.getString(Constants.KEY_USER_ID);


        //Delivery
        deliveryViewModel = new ViewModelProvider(this).get(DeliveryViewModel.class);
        //Profile
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding.deliveryDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                onBackPressed();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("Class").equals("Profile")) {
            binding.deliveryAddressAddFullname.setVisibility(View.VISIBLE);
            binding.deliveryPaymentTitle.setText("Update");
            binding.deliveryTitle.setText("Edit Profile");
            binding.deliveryDescription.setText("Profile");
            binding.deliveryAddressTwo.setVisibility(View.GONE);

            profileViewModel.getUserProfileDetails(UserID).observe(this, new Observer<List<UserModel>>() {
                @Override
                public void onChanged(List<UserModel> userModels) {
                    if (userModels.size() > 0) {
                        for (UserModel userModel : userModels) {
                            binding.deliveryAddressAddFullname.setText(userModel.getUser_name());
                            binding.deliveryAddressAddHouse.setText(userModel.getHouse_or_flat());
                            binding.deliveryAddressAddArea.setText(userModel.getArea_street());
                            binding.deliveryAddressAddState.setText("TamilNadu");
                            binding.deliveryAddressAddCity.setText(userModel.getCity());
                            binding.deliveryAddressAddTaluk.setText(userModel.getTaluk());
                            binding.deliveryAddressAddPincode.setText(userModel.getPincode());
                            binding.deliveryAddressAddEmail.setText(userModel.getUser_email());

                            preferManager.putString(Constants.KEY_USER_FULL_NAME, userModel.getUser_name());
                        }
                    }

                }
            });

            binding.deliveryDetailsPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String FullName = binding.deliveryAddressAddFullname.getText().toString();
                    String House = binding.deliveryAddressAddHouse.getText().toString();
                    String Area = binding.deliveryAddressAddArea.getText().toString();
                    String State = binding.deliveryAddressAddState.getText().toString();
                    String City = binding.deliveryAddressAddCity.getText().toString();
                    String Taluk = binding.deliveryAddressAddTaluk.getText().toString();
                    String Pincode = binding.deliveryAddressAddPincode.getText().toString();
                    String Email = binding.deliveryAddressAddEmail.getText().toString();
                    if (FullName.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter Your Full Name", Toast.LENGTH_SHORT).show();
                    } else if (House.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter Your House Details", Toast.LENGTH_SHORT).show();
                    } else if (Area.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter your Area", Toast.LENGTH_SHORT).show();
                    } else if (State.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter your State", Toast.LENGTH_SHORT).show();
                    } else if (City.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter your City", Toast.LENGTH_SHORT).show();
                    } else if (Taluk.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter your Taluk", Toast.LENGTH_SHORT).show();
                    } else if (Pincode.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter your Pincode", Toast.LENGTH_SHORT).show();
                    } else {
                        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().AddProfileDetails(UserID, Email, House, Area, State, City, Taluk, Pincode, FullName);
                        call.enqueue(new Callback<APIResponse>() {
                            @Override
                            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                if (response.code() == 200) {
                                    if (response.body().getSuccess() != null) {
                                        onBackPressed();

                                        preferManager.putString(Constants.KEY_USER_FULL_NAME, FullName);
                                    }

                                } else if (response.code() == 500) {
                                    Toast.makeText(getApplicationContext(), "Internal Server error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponse> call, Throwable t) {

                            }
                        });
                    }
                }
            });
        } else {
            profileViewModel.getUserProfileDetails(UserID).observe(this, new Observer<List<UserModel>>() {
                @Override
                public void onChanged(List<UserModel> userModels) {
                    if (userModels.size() > 0) {
                        for (UserModel userModel : userModels) {
                            binding.deliveryAddressAddFullname.setText(userModel.getUser_name());
                            binding.deliveryAddressAddHouse.setText(userModel.getHouse_or_flat());
                            binding.deliveryAddressAddArea.setText(userModel.getArea_street());
                            binding.deliveryAddressAddState.setText("TamilNadu");
                            binding.deliveryAddressAddCity.setText(userModel.getCity());
                            binding.deliveryAddressAddTaluk.setText(userModel.getTaluk());
                            binding.deliveryAddressAddPincode.setText(userModel.getPincode());
                            binding.deliveryAddressAddEmail.setText(userModel.getUser_email());

                            preferManager.putString(Constants.KEY_USER_FULL_NAME, userModel.getUser_name());
                        }
                    }

                }
            });

            binding.deliveryDetailsPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String FullName = binding.deliveryAddressAddFullname.getText().toString();
                    String House = binding.deliveryAddressAddHouse.getText().toString();
                    String Area = binding.deliveryAddressAddArea.getText().toString();
                    String State = binding.deliveryAddressAddState.getText().toString();
                    String City = binding.deliveryAddressAddCity.getText().toString();
                    String Taluk = binding.deliveryAddressAddTaluk.getText().toString();
                    String Pincode = binding.deliveryAddressAddPincode.getText().toString();
                    String Email = binding.deliveryAddressAddEmail.getText().toString();
                    if (FullName.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter Your Full Name", Toast.LENGTH_SHORT).show();
                    } else if (House.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter Your House Details", Toast.LENGTH_SHORT).show();
                    } else if (Area.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter your Area", Toast.LENGTH_SHORT).show();
                    } else if (State.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter your State", Toast.LENGTH_SHORT).show();
                    } else if (City.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter your City", Toast.LENGTH_SHORT).show();
                    } else if (Taluk.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter your Taluk", Toast.LENGTH_SHORT).show();
                    } else if (Pincode.isEmpty()) {
                        Toast.makeText(Delivery.this, "please enter your Pincode", Toast.LENGTH_SHORT).show();
                    } else {
                        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().AddProfileDetails(UserID, Email, House, Area, State, City, Taluk, Pincode, FullName);
                        call.enqueue(new Callback<APIResponse>() {
                            @Override
                            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                if (response.code() == 200) {
                                    if (response.body().getSuccess() != null) {
                                        Intent intent = new Intent(Delivery.this, Payment.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        preferManager.putString(Constants.KEY_USER_FULL_NAME, FullName);
                                    }

                                } else if (response.code() == 500) {
                                    Toast.makeText(getApplicationContext(), "Internal Server error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponse> call, Throwable t) {

                            }
                        });
                    }
                }
            });
        }

    }
}