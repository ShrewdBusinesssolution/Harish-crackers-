package com.vedi.vedi_box.views.delivery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vedi.vedi_box.models.UserModel;
import com.vedi.vedi_box.repositories.DeliveryRepo;

import java.util.List;

public class DeliveryViewModel extends ViewModel {

    DeliveryRepo deliveryRepo = new DeliveryRepo();

    public LiveData<List<UserModel>> getUserAddressDetails(String userID) {
        return deliveryRepo.getUserAddressDetails(userID);
    }
}