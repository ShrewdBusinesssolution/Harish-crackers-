package com.vedi.vedi_box.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vedi.vedi_box.models.Order;
import com.vedi.vedi_box.utilities.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepo {

    private static final String TAG = "PaymentRepo";

    private final MutableLiveData<List<Order>> mutableOrder = new MutableLiveData<>();

    public LiveData<List<Order>> getOrderDetails(String userID) {
        initOrderDetails(userID);
        return mutableOrder;
    }

    private void initOrderDetails(String userID) {
        List orderDetailsList = new ArrayList();

        Call<Order> call = RetrofitClient.getInstance().getMyApi().OrdersDetails(userID);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.code() == 200) {
                    Order order = response.body();
                    orderDetailsList.add(new Order(order.getOrder_id(), order.getOrder_date(), order.getExpected_date(), order.getTotal_price()));
                    mutableOrder.setValue(orderDetailsList);
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }
}
