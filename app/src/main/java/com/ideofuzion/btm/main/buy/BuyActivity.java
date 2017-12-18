package com.ideofuzion.btm.main.buy;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.locationservice.LocationUpdateService;
import com.ideofuzion.btm.main.sell.SellBitcoinActivity;
import com.ideofuzion.btm.main.settings.SettingsActivity;
import com.ideofuzion.btm.main.scanqr.ScanQRActivity;
import com.ideofuzion.btm.main.transfercomplete.TransferCompleteActivity;
import com.ideofuzion.btm.network.VolleyRequestHelper;
import com.ideofuzion.btm.utils.AlertMessage;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.MyUtils;
import com.ideofuzion.btm.utils.PermissionHandler;
import com.ideofuzion.btm.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by khali on 6/1/2017.
 */

public class BuyActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener {
    public static final String ACTION_UPDATE_LOCATION = "com.ideofuzion.btm.locationUpdate";
    public static final String EXTRA_IS_BUYING = "isBuying";
    private static final int LOCATION_PERMISSION_ID = 1;
    private Typeface fontBold;
    Button button_buyActivity_sellBitcoins;
    private final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    private final int REQUEST_CHECK_SETTINGS = 2;
    Intent serviceIntent;
    Boolean hasSession = false;
    private Typeface fontRegular;
    DialogHelper dialogHelper;
    String sellingRate, buyingRate;
    TextView text_buy_buyingAt;
    TextView text_buy_buyingRate;
    TextView text_buy_sellingAt;
    TextView text_buy_sellingRate;
    ImageView imageView_buy_settings;
    private String dollarRate;
    private Button button_buyActivity_buyBitcoins;
    private PermissionHandler permissionHandler;


    /*
        QRGEncoder qrgEncoder = new QRGEncoder(address, null, QRGContents.Type.TEXT, Constants.QR_CODE_SIZE);
        try {
            // Getting QR-Code as Bitmap
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            imageView_qrCode_qr.setImageBitmap(bitmap);
            // Setting Bitmap to ImageView
        } catch (Exception e) {
            Log.v(TAG, e.toString());
        }*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_buy);
            initResources();

            iniTypefaces();

            addListener();

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
        }
    }

    private void addListener() {
        button_buyActivity_buyBitcoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(BuyActivity.this, ScanQRActivity.class)
                            .putExtra(EXTRA_IS_BUYING, true));
            }
        });
        button_buyActivity_sellBitcoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyActivity.this, SellBitcoinActivity.class));
//                    startActivity(new Intent(BuyActivity.this, ScanQRActivity.class)
//                            .putExtra(EXTRA_IS_BUYING, false));
/*
                AlertMessage.showError(button_buyActivity_sellBitcoins,"Not Available!");
*/
            }
        });
    }

    private void iniTypefaces() {
        button_buyActivity_sellBitcoins.setTypeface(fontBold);
        button_buyActivity_buyBitcoins.setTypeface(fontBold);
        text_buy_buyingAt.setTypeface(fontBold);
        text_buy_buyingRate.setTypeface(fontRegular);
        text_buy_sellingAt.setTypeface(fontBold);
        text_buy_sellingRate.setTypeface(fontRegular);
    }

    private void initResources() {
        permissionHandler = new PermissionHandler(this);
        dialogHelper = new DialogHelper(this);
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();

        button_buyActivity_sellBitcoins = (Button) findViewById(R.id.button_buyActivity_sellBitcoins);
        button_buyActivity_buyBitcoins = (Button) findViewById(R.id.button_buyActivity_buyBitcoins);

        text_buy_buyingAt = (TextView) findViewById(R.id.text_buy_buyingAt);
        text_buy_buyingRate = (TextView) findViewById(R.id.text_buy_buyingRate);
        text_buy_sellingAt = (TextView) findViewById(R.id.text_buy_sellingAt);
        text_buy_sellingRate = (TextView) findViewById(R.id.text_buy_sellingRate);

        imageView_buy_settings = (ImageView) findViewById(R.id.imageView_buy_settings);
        imageView_buy_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyActivity.this, SettingsActivity.class));
            }
        });


      /*  if (BTMApplication.getInstance().getBTMUserObj() != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Your Bitcoin Address Key")
                    .setMessage(BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId())
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        getUpdatedRates();

    }


    public void getUpdatedRates() {

        String url = "https://blockchain.info/ticker";
        VolleyRequestHelper.getRequestWithParams(url, new HashMap<String, String>(), this);
        dialogHelper.showProgressDialog();

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        dialogHelper.hideProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        dialogHelper.hideProgressDialog();
        if (response != null) {
            Log.e("bitcons", response.toString());
            try {
                JSONObject jsonObject = new JSONObject(response.getString("GBP"));
                dollarRate = jsonObject.getString("sell");
                BTMApplication.getInstance().setOriginalSellingRate(dollarRate);

                Double margin = (Double.parseDouble(BTMApplication.getInstance().getBTMUserObj().getMerchantProfitMargin()) / 100);

                Double sellRate = Double.parseDouble(jsonObject.getString("sell")) * margin;
                Double buyRate = Double.parseDouble(jsonObject.getString("buy")) * margin;

                sellRate = Double.parseDouble(jsonObject.getString("sell")) + sellRate;
                buyRate = Double.parseDouble(jsonObject.getString("buy")) - buyRate;
                sellingRate = String.valueOf(sellRate);
                buyingRate = String.valueOf(buyRate);

                text_buy_buyingRate.setText("£ " + String.format("%.2f", sellRate));
                text_buy_sellingRate.setText("£ " + String.format("%.2f", buyRate));
                BTMApplication.getInstance().setSellingRate(sellingRate);
                BTMApplication.getInstance().setBuyingRate(buyingRate);
                checkLocationAccessPermission();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    public boolean validateFields() {
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getEthereumUserPasscode())) {
            AlertMessage.showError(button_buyActivity_sellBitcoins, "Please setup PIN code from settings");
            return false;
        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getKrakenAPIKey())) {
            AlertMessage.showError(button_buyActivity_sellBitcoins, "Please complete Kraken setup from settings");
            return false;
        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getKrakenAPISecret())) {
            AlertMessage.showError(button_buyActivity_sellBitcoins, "Please complete Kraken setup from settings");
            return false;
        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getProfitWalletKrakenBenificiaryKey())) {
            AlertMessage.showError(button_buyActivity_sellBitcoins, "Please complete Profit Wallet setup from settings");
            return false;
        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getProfitWalletAddress())) {
            AlertMessage.showError(button_buyActivity_sellBitcoins, "Please complete Profit Wallet setup from settings");
            return false;
        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getBitpointProfitWalletAddress())) {
            AlertMessage.showError(button_buyActivity_sellBitcoins, "Please complete Bitpoint Profit Wallet setup from settings");
            return false;
        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getMerchantProfitThreshold())) {
            AlertMessage.showError(button_buyActivity_sellBitcoins, "Please complete Merchant Profit Threshold setup from settings");
            return false;
        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getHotWalletBenificiaryKey())) {
            AlertMessage.showError(button_buyActivity_sellBitcoins, "Please update hot wallet beneficiary from settings");
            return false;
        }
        return true;
    }

    public boolean checkLocationAccessPermission() {

        if (!permissionHandler.isPermissionAvailable(PermissionHandler.LOCATION_COARSE)
                && !permissionHandler.isPermissionAvailable(PermissionHandler.LOCATION_FINE)) {
            permissionHandler.requestMultiplePermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_ID);
            return false;
        }
        checkLocationEnabled();
        return true;
    }

    private void checkLocationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
            displayLocationSettingsRequest(this);
        } else {
            startService(new Intent(BuyActivity.this, LocationUpdateService.class));
        }
    }
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(BuyActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                checkLocationAccessPermission();
            }/* else {
                if (!permissionHandler.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    getActivity().startActivityForResult(new Intent(getActivity(), PermissionActivity.class)
                            .putExtra(PermissionActivity.PARAM_PERMISSION_LOCATION, true), PERMISSION_ACTIVITY_ID);
                }
            }*/
        }
    }

}
