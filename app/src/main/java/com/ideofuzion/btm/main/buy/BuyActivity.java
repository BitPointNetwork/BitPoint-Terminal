package com.ideofuzion.btm.main.buy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.location.LocationSettingsRequest;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.settings.SettingsActivity;
import com.ideofuzion.btm.location.LocationUpdateService;
import com.ideofuzion.btm.main.scanqr.ScanQRActivity;
import com.ideofuzion.btm.network.VolleyRequestHelper;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by khali on 6/1/2017.
 */

public class BuyActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener{
    public static final String ACTION_UPDATE_LOCATION = "com.ideofuzion.btm.locationUpdate";
    private Typeface fontBold;
    Button button_buyActivity_sellBitcoins;
    private final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    protected LocationSettingsRequest mLocationSettingsRequest;
    private final int REQUEST_CHECK_SETTINGS = 2;
    Intent serviceIntent;
    Boolean hasSession = false;
    private Typeface fontRegular;
    DialogHelper dialogHelper;

    ImageView imageView_buy_settings;
    private String dollarRate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_buy);
            initResources();

            iniTypefaces();

            addListener();

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }catch (Exception e)
        {}
    }

    private void addListener() {
        button_buyActivity_sellBitcoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyActivity.this, ScanQRActivity.class)
                .putExtra("dollarRate",dollarRate));
            }
        });
    }

    private void iniTypefaces() {
        button_buyActivity_sellBitcoins.setTypeface(fontBold);
    }

    private void initResources() {
        dialogHelper = new DialogHelper(this);
        if (BTMApplication.getInstance().getBTMUserObj() == null)
            if (SessionManager.getInstance(this).hasSession()) {
                hasSession = true;
            }
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();

        button_buyActivity_sellBitcoins = (Button) findViewById(R.id.button_buyActivity_sellBitcoins);


        imageView_buy_settings = (ImageView) findViewById(R.id.imageView_buy_settings);
        imageView_buy_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyActivity.this, SettingsActivity.class));
            }
        });
        imageView_buy_settings.setVisibility(View.GONE);

        getUpdatedRates();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasSession) {
            /*if (checkLocationAccessPermissions()) {
                checkIfLocationServicesAreEnabled();
            }*/
        }
    }

    private boolean checkLocationAccessPermissions() {
        if ((ContextCompat.checkSelfPermission(BuyActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(BuyActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(BuyActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_LOCATION);
            return false;
        }
        startLocationUpdateService();
        return true;
    }

    public boolean checkIfLocationServicesAreEnabled() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            SpannableString spannableStringTitle = new SpannableString("Need location service");
            spannableStringTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                    0, spannableStringTitle.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableStringTitle.setSpan(new Fonts.MultiCustomTypeFaceSpan("", fontBold),
                    0, spannableStringTitle.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            dialog.setTitle(spannableStringTitle);
            SpannableString spannableStringMessage = new SpannableString("Bitpoint would like to use your location.\nPlease enable location services.");
            spannableStringMessage.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                    0, spannableStringMessage.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableStringMessage.setSpan(new Fonts.MultiCustomTypeFaceSpan("", fontBold),
                    0, spannableStringMessage.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            dialog.setMessage(spannableStringMessage);
            SpannableString spannableStringOpenSettings = new SpannableString("GO TO SETTINGS");
            spannableStringOpenSettings.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),
                    0, spannableStringOpenSettings.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableStringOpenSettings.setSpan(new Fonts.MultiCustomTypeFaceSpan("", fontRegular),
                    0, spannableStringOpenSettings.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            dialog.setPositiveButton(spannableStringOpenSettings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });

            SpannableString spannableStringCancel = new SpannableString("CANCEL");
            spannableStringCancel.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),
                    0, spannableStringCancel.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableStringCancel.setSpan(new Fonts.MultiCustomTypeFaceSpan("", fontRegular),
                    0, spannableStringCancel.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            dialog.setNegativeButton(spannableStringCancel, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
        }

        return gps_enabled || network_enabled;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if ((ContextCompat.checkSelfPermission(BuyActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(BuyActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED)) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    checkIfLocationServicesAreEnabled();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void startLocationUpdateService() {
        serviceIntent = new Intent(BuyActivity.this, LocationUpdateService.class);
        startService(serviceIntent);
    }


    public void getUpdatedRates()
    {

        String url = "https://blockchain.info/ticker";
        VolleyRequestHelper.getRequestWithParams(url,new HashMap<String, String>(),this);
        dialogHelper.showProgressDialog();

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        dialogHelper.hideProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        dialogHelper.hideProgressDialog();
        if(response!= null)
        {
            Log.e("bitcons",response.toString());
            try {
                JSONObject jsonObject = new JSONObject(response.getString("USD"));
                dollarRate = jsonObject.getString("sell");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
