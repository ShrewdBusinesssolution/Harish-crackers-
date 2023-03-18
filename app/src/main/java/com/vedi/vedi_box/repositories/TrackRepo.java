package com.vedi.vedi_box.repositories;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.utilities.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackRepo {

    private static final String TAG = "TrackRepo";

    private final MutableLiveData<List<Product>> mutableTrackList = new MutableLiveData<>();

    public LiveData<List<Product>> getTrackList(Activity activity, String userID, String orderID) {
        getOrderTrackList(activity, userID, orderID);
        return mutableTrackList;
    }

    private void getOrderTrackList(Activity activity, String userID, String orderID) {

        Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().OrderTrackView(userID, orderID);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.code() == 200) {
                    APIResponse apiResponse = response.body();
                    List<Product> trackList = new ArrayList(Arrays.asList(apiResponse.getOrder_details()));

                    mutableTrackList.setValue(trackList);
                } else if (response.code() == 500) {
                    Toast.makeText(activity, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });

    }


}
