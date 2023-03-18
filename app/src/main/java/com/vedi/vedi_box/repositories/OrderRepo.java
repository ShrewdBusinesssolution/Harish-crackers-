package com.vedi.vedi_box.repositories;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.Order;
import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.utilities.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepo {
    private static final String TAG = "OrderRepo";

    private final MutableLiveData<List<Order>> mutableOrderList = new MutableLiveData<>();


    public LiveData<List<Order>> getOrders(String userID, Activity activity) {
        getOrderList(userID, activity);
        return mutableOrderList;
    }

    private void getOrderList(String userID, Activity activity) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Call<APIResponse> call = RetrofitClient.getInstance().getMyApi().OrdersView(userID);

                call.enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        if (response.code() == 200) {
                            APIResponse apiResponse = response.body();
                            if (response.body().getSuccess() != null && response.body().getSuccess().equals("success")) {
                                List<Order> orderList = new ArrayList(Arrays.asList(apiResponse.getMy_orders()));
                                for (Order order : orderList) {
                                    Log.d(TAG, "ORDER-SUCCESS:::" + order.getOrder_id());

                                    mutableOrderList.setValue(orderList);
                                }
                            } else {
                                List orderList = new ArrayList();
                                mutableOrderList.setValue(orderList);
                            }

                        } else if (response.code() == 500) {
                            Toast.makeText(activity, "Internal Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        Log.d(TAG, "ORDER-FAILURE:::" + t.getMessage());

                    }
                });
            }
        });
        thread.start();

    }
}
