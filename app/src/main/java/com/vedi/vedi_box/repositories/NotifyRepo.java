package com.vedi.vedi_box.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.Notify;
import com.vedi.vedi_box.utilities.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyRepo {
    private static final String TAG = "NotifyRepo";

    private final MutableLiveData<List<Notify>> mutableNotificationList = new MutableLiveData<>();

    public LiveData<List<Notify>> getNotifications() {
        loadNotification();
        return mutableNotificationList;
    }

    private void loadNotification() {

        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().getNotification();

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    APIResponse apiResponse = response.body();
                    if (response.body().getSuccess() != null && response.body().getSuccess().equals("success")) {
                        List<Notify> notifyList = new ArrayList(Arrays.asList(apiResponse.getNotification_details()));

                        mutableNotificationList.setValue(notifyList);
                    } else {
                        List notifyList = new ArrayList();
                        mutableNotificationList.setValue(notifyList);
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });

    }
}
