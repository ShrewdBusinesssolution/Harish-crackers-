package com.vedi.vedi_box.views.order;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vedi.vedi_box.models.Order;
import com.vedi.vedi_box.repositories.OrderRepo;

import java.util.List;

public class OrdersViewModel extends ViewModel {
    OrderRepo orderRepo = new OrderRepo();


    public LiveData<List<Order>> getOrders(String userID, Activity activity) {
        return orderRepo.getOrders(userID, activity);
    }
}