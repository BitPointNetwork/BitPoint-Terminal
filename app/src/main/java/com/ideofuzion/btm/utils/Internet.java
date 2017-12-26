package com.ideofuzion.btm.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Ideofuzion on 4/12/2017.
 *
 * this class is used to check internet connection all around the application
 */

//TODO use MyUtils class function isInternetConnected and remove this class
public class Internet {
    public static boolean isConnected(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
