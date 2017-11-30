package com.ideofuzion.btm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ideofuzion.btm.main.sell.SellBitcoinActivity;

import org.json.JSONObject;

/**
 * Created by user on 11/1/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("message", remoteMessage.getData().toString());
        try {
            JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
            String senderAddresss = jsonObject1.getString("senderAddress");
            String amount = jsonObject1.getString("amount");

            sendNotification("Transaction Confirmation", "You" +
                    " received " + amount + " BTC from this address " + senderAddresss, 0);

            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(SellBitcoinActivity.SUCCESS_CONFRIMATION_ACTION)
                    .putExtra("senderAddress", senderAddresss)
                    .putExtra("amount", amount));

        } catch (Exception e) {
        }
    }

    private void sendNotification(String title, String message, int id) {
        Intent intent = new Intent();

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification n = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setContentIntent(pIntent)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000})
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(id, n);


    }
}
