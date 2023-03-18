package com.vedi.vedi_box.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM", "Token:" + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            Log.d("FCM", "Remote message received:" + remoteMessage.getNotification().getBody());

            String Title = remoteMessage.getNotification().getTitle();
            String Body = remoteMessage.getNotification().getBody();
            String Type = remoteMessage.getNotification().getClickAction();

            if (Type.equals("withImage")) {
                String Image = remoteMessage.getNotification().getImageUrl().toString();

                NotificationHelper helper = new NotificationHelper(this);
                helper.triggerNotification(Title, Body, Image);
            } else {
                NotificationHelper helper = new NotificationHelper(this);
                helper.triggerNotification(Title, Body);

            }
        }

    }
}
