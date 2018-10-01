package com.fabuleux.wuntu.billstore.Fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Dell on 6/8/16.
 */

public class MyFcmListenerService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("TAG","MESSAGE RECIEVED");
    }

    /*@Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }*/
}