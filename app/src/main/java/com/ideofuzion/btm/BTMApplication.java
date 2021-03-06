package com.ideofuzion.btm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.PowerManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ideofuzion.btm.model.BTMUser;
import com.ideofuzion.btm.model.QRModel;
import com.splunk.mint.Mint;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Ideofuzion on 6/6/2017.
 *
 * this is the application class that's
 * lifecycle is associated with application all data that is necessary
 * during running the application is maintained in this class
 *
 */

public class BTMApplication extends MultiDexApplication {

    private static final String TAG = "BitPoint";
    private RequestQueue mRequestQueue;
    private static BTMApplication ourInstance;
    BTMUser btmUser;

    public String getOriginalSellingRate() {
        return originalSellingRate;
    }

    public void setOriginalSellingRate(String originalSellingRate) {
        this.originalSellingRate = originalSellingRate;
    }

    String originalSellingRate;
    public FirebaseAnalytics firebaseAnalytics;

    public String getSellingRate() {
        return sellingRate;
    }

    public void setSellingRate(String sellingRate) {
        this.sellingRate = sellingRate;
    }

    String sellingRate;

    public String getBuyingRate() {
        return buyingRate;
    }

    public void setBuyingRate(String buyingRate) {
        this.buyingRate = buyingRate;
    }

    String buyingRate;
    public Hashtable<String, String> contactNamesHashMap = new Hashtable<>();

    public QRModel getQrModel() {
        return qrModel;
    }

    public void setQrModel(QRModel qrModel) {
        this.qrModel = qrModel;
    }

    QRModel qrModel;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static BTMApplication getInstance() {
        return ourInstance;
    }

    public BTMApplication() {
    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        return firebaseAnalytics == null ? FirebaseAnalytics.getInstance(this) : firebaseAnalytics;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Mint.initAndStartSession(this, "6fb67f85");
        } catch (Throwable ignore) {
            // ignored
        }
        FirebaseApp.initializeApp(this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ourInstance = this;

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }//end of getrequest queu


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setBTMUserObj(BTMUser btmUser) {
        this.btmUser = btmUser;
    }

    public BTMUser getBTMUserObj() {
        return btmUser;
    }

}
