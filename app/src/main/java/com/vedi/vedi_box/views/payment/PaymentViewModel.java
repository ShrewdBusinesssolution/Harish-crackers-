package com.vedi.vedi_box.views.payment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vedi.vedi_box.models.Order;
import com.vedi.vedi_box.repositories.PaymentRepo;

import java.util.List;

public class PaymentViewModel extends ViewModel {

    PaymentRepo paymentRepo = new PaymentRepo();

    public LiveData<List<Order>> getOrderDetails(String userID) {
        return paymentRepo.getOrderDetails(userID);
    }
}