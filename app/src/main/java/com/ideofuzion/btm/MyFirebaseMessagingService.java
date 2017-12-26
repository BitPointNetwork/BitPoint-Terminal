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
 * Created by ideofuzion on 11/1/2017.
 *
 * this class's onMessageReceived is called
 * each time a notification is received the data from the
 * notification is parsed and actions are performed based upon that
 * data
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("message", remoteMessage.getData().toString());
        try {
           String type= remoteMessage.getData().get("type");
            if(type.equalsIgnoreCase("transactionUnConfirmed")){
                String txId = remoteMessage.getData().get("txId");
                String amount = remoteMessage.getData().get("amount");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(SellBitcoinActivity.SUCCESS_CONFRIMATION_ACTION)
                        .putExtra("txId", txId)
                        .putExtra("amount", amount)
                        .putExtra("type",type));
            }else{
                String txId = remoteMessage.getData().get("txId");
                String amount = remoteMessage.getData().get("amount");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(SellBitcoinActivity.SUCCESS_CONFRIMATION_ACTION)
                        .putExtra("txId", txId)
                        .putExtra("amount", amount)
                        .putExtra("type",type));
                sendNotification("Transaction Confirmation", "You" +
                        " received " + amount + " BTC In Transaction Hash " + txId, 0);

            }



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
