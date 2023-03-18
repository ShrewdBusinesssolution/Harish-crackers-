package com.vedi.vedi_box.views.track_order;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vedi.vedi_box.models.Product;
import com.vedi.vedi_box.repositories.TrackRepo;

import java.util.List;

public class TrackViewModel extends ViewModel {
    TrackRepo trackRepo = new TrackRepo();


    public LiveData<List<Product>> getTrackList(Activity activity, String userID, String orderID) {
        return trackRepo.getTrackList(activity, userID, orderID);
    }
}