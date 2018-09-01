package com.fahim.onlinequiz.BroadcastReciever;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by HSBC on 19-02-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        handleNotification(remoteMessage.getNotification().getBody());
    }

    private void handleNotification(String body) {
        Intent intent = new Intent("pushNotification");
        intent.putExtra("message", body);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
