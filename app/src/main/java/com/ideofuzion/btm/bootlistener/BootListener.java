package com.ideofuzion.btm.bootlistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ideofuzion.btm.main.buy.BuyActivity;

/**
 * Created by ideofuzion on 9/14/2017.
 *
 * this class this broadcast receiver and
 * will receive a broadcast each time the
 * tablet will restart
 */

public class BootListener extends BroadcastReceiver {
    /**
     * this function will receive a broadcast
     * when the tablet restarts
     * and the app will launch in result
     *
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, BuyActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
