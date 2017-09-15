package com.ideofuzion.btm.bootlistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ideofuzion.btm.main.buy.BuyActivity;

/**
 * Created by khali on 9/14/2017.
 */

public class BootListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, BuyActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
