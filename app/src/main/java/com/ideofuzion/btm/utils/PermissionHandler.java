package com.ideofuzion.btm.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Yasir on 1/13/2016.
 */
public class PermissionHandler {
    Activity context;
    PermissionInfo[] requestedPermissionsInfo;
    public final int PERMISSIONS_REQUEST = 786;
    public static final String C2D_MESSAGE = "com.nextin.watchmaster.permission.C2D_MESSAGE";
    public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";
    public static final String PROVIDE_BACKGROUND = "com.google.android.permission.PROVIDE_BACKGROUND";
    public static final String WAKE_LOCK = "android.permission.WAKE_LOCK";
    public static final String RECEIVE = "com.google.android.c2dm.permission.RECEIVE";
    public static final String BILLING = "com.android.vending.BILLING";
    public static final String INTERNET = "android.permission.INTERNET";
    public static final String ACCESS_WIFI_STATE = "android.permission.ACCESS_WIFI_STATE";
    public static final String ACCESS_NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";
    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

    public static final String LOCATION_COARSE = "android.permission.ACCESS_COARSE_LOCATION";
    public static final String LOCATION_FINE =   "android.permission.ACCESS_FINE_LOCATION";

    public PermissionHandler(Activity context) {
        this.context = context;
    }

    public boolean isMarshMallow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    void checkRequestedPermissions() {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.nextin.watchmaster", PackageManager.GET_PERMISSIONS);
            requestedPermissionsInfo = packageInfo.permissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isPermissionAvailable(String permission) {


        if (isMarshMallow() && ContextCompat.checkSelfPermission(this.context, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    public boolean requestPermission(String permission) {
        if (isPermissionAvailable(permission)) {
            return true;
        }
        if (isMarshMallow()) {
            ActivityCompat.requestPermissions(this.context, new String[]{permission}, PERMISSIONS_REQUEST);
        }
        return false;
    }

    public boolean requestPermission(String permission, int permisionRequest) {
        if (isPermissionAvailable(permission)) {
            return true;
        }
        if (isMarshMallow()) {
            ActivityCompat.requestPermissions(this.context, new String[]{permission}, permisionRequest);
        }
        return false;
    }

    public boolean requestMultiplePermission(String[] permission, int ACCOUNT_PERMISSION_ID) {
        if (isMarshMallow()) {
            ActivityCompat.requestPermissions(this.context, permission, ACCOUNT_PERMISSION_ID);
        }
        return false;
    }
}
