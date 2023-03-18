package com.vedi.vedi_box.views.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vedi.vedi_box.models.Notify;
import com.vedi.vedi_box.repositories.NotifyRepo;

import java.util.List;

public class NotificationViewModel extends ViewModel {

    NotifyRepo notifyRepo = new NotifyRepo();

    public LiveData<List<Notify>> getNotifications() {
        return notifyRepo.getNotifications();
    }


}