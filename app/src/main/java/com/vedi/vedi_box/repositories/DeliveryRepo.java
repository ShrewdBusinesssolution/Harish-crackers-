package com.vedi.vedi_box.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.UserModel;
import com.vedi.vedi_box.utilities.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryRepo {
    private static final String TAG = "DeliveryRepo";

    private final MutableLiveData<List<UserModel>> mutableUser = new MutableLiveData<>();

    public LiveData<List<UserModel>> getUserAddressDetails(String userID) {
        initUserAddressDetails(userID);
        return mutableUser;
    }

    private void initUserAddressDetails(String userID) {
        List UserDetailsList = new ArrayList();

        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().ViewUserDetails(userID);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse = response.body();
                if (response.body().getSuccess() != null && response.body().getSuccess().equals("success")) {
                    List<UserModel> UserDetailsList = new ArrayList(Arrays.asList(apiResponse.getUser_details()));

                    mutableUser.setValue(UserDetailsList);

                } else {
                    mutableUser.setValue(UserDetailsList);

                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }
}
