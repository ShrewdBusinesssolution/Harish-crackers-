package com.vedi.vedi_box.views.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vedi.vedi_box.models.APIResponse;
import com.vedi.vedi_box.models.UserModel;
import com.vedi.vedi_box.repositories.ProfileRepo;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    ProfileRepo profileRepo = new ProfileRepo();

    public LiveData<List<UserModel>> getUserProfileDetails(String userID) {
        return profileRepo.getUserProfileDetails(userID);
    }

    public LiveData<List<APIResponse>> getOrderCount(String userID) {
        return profileRepo.getOrderCount(userID);
    }
}