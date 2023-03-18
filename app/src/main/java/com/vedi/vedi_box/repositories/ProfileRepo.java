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

public class ProfileRepo {
    private static final String TAG = "ProfileRepo";

    private final MutableLiveData<List<UserModel>> mutableUser = new MutableLiveData<>();
    private final MutableLiveData<List<APIResponse>> mutableOrderCount = new MutableLiveData<>();

    public LiveData<List<UserModel>> getUserProfileDetails(String userID) {
        initUserProfileDetails(userID);
        return mutableUser;
    }

    public LiveData<List<APIResponse>> getOrderCount(String userID) {
        initOrderCount(userID);
        return mutableOrderCount;
    }

    private void initUserProfileDetails(String userID) {


        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().ViewProfile(userID);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if (response.code() == 200) {
                    APIResponse apiResponse = response.body();
                    if (response.body().getSuccess() != null && response.body().getSuccess().equals("success")) {
                        List<UserModel> UserDetailsList = new ArrayList(Arrays.asList(apiResponse.getUser_details()));

                        mutableUser.setValue(UserDetailsList);

                    } else {
                        List UserDetailsList = new ArrayList();
                        mutableUser.setValue(UserDetailsList);

                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

    private void initOrderCount(String userID) {
        List<APIResponse> orderDetails = new ArrayList<>();

        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().OrderCount(userID);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    APIResponse apiResponse = response.body();

                    orderDetails.add(new APIResponse(apiResponse.getTotal_orders(), apiResponse.getPurchase_amount()));

                    mutableOrderCount.setValue(orderDetails);
                } else {

                    mutableOrderCount.setValue(orderDetails);
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }
}
