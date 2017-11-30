package com.ideofuzion.btm.locationservice;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.network.VolleyRequestHelper;
import com.ideofuzion.btm.utils.Constants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khali on 5/23/2017.
 */

public class LocationUpdateService extends Service implements Response.Listener<JSONObject>, Response.ErrorListener {

    public LocationManager mLocationManager;
    public LocationUpdaterListener mLocationListener;
    public Location previousBestLocation = null;
    long delayTime = 60000;


    public LocationUpdateService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationListener = new LocationUpdaterListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            startListening();
        } else {
            stopListening();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, delayTime, 0, mLocationListener);
            else if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, delayTime, 0, mLocationListener);
        }
    }

    private void stopListening() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }




    public class LocationUpdaterListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            previousBestLocation = location;
            try {
                Log.d("lcoation updates", location.toString());
                if (BTMApplication.getInstance().getBTMUserObj() != null) {
/*
                    sendLocationUpdateRequestToServer(location);
*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            stopListening();
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }


    //sending location updates to server
    public void sendLocationUpdateRequestToServer(Location location) {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_UPDATE_LAT_LNG;
        Map<String, String> param = new HashMap<>();
        param.put("lat", location.getLatitude() + "");
        param.put("long", location.getLongitude() + "");
        param.put("userId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        VolleyRequestHelper.sendPostRequestWithParam(url, param, this);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("test", "server error");

    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("test", response.toString());
    }
}
