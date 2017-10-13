package com.ideofuzion.btm.main.buy;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.location.LocationSettingsRequest;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.settings.SettingsActivity;
import com.ideofuzion.btm.main.scanqr.ScanQRActivity;
import com.ideofuzion.btm.main.transfercomplete.TransferCompleteActivity;
import com.ideofuzion.btm.network.VolleyRequestHelper;
import com.ideofuzion.btm.utils.AlertMessage;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.MyUtils;
import com.ideofuzion.btm.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by khali on 6/1/2017.
 */

public class BuyActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener {
    public static final String ACTION_UPDATE_LOCATION = "com.ideofuzion.btm.locationUpdate";
    public static final String EXTRA_IS_BUYING = "isBuying";
    private Typeface fontBold;
    Button button_buyActivity_sellBitcoins;
    private final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    protected LocationSettingsRequest mLocationSettingsRequest;
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
                if (validateFields())
                    startActivity(new Intent(BuyActivity.this, ScanQRActivity.class)
                            .putExtra("dollarRate", dollarRate)
                            .putExtra(EXTRA_IS_BUYING, true));
            }
        });
        button_buyActivity_sellBitcoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields())
                    startActivity(new Intent(BuyActivity.this, ScanQRActivity.class)
                            .putExtra("dollarRate", dollarRate)
                            .putExtra(EXTRA_IS_BUYING, false));
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


        if (BTMApplication.getInstance().getBTMUserObj() != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Your Bitcoin Address Key")
                    .setMessage(BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId())
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }


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
                JSONObject jsonObject = new JSONObject(response.getString("USD"));
                dollarRate = jsonObject.getString("sell");
                BTMApplication.getInstance().getBTMUserObj().setBitcoinDollarRate(dollarRate);


                Double margin = (Double.parseDouble(BTMApplication.getInstance().getBTMUserObj().getMerchantProfitMargin()) / 100);

                Double sellRate = Double.parseDouble(jsonObject.getString("sell")) * margin;
                Double buyRate = Double.parseDouble(jsonObject.getString("buy")) * margin;

                sellRate = Double.parseDouble(jsonObject.getString("sell")) + sellRate;
                buyRate = Double.parseDouble(jsonObject.getString("buy")) - buyRate;
                sellingRate = String.valueOf(sellRate);
                buyingRate = String.valueOf(buyRate);

                text_buy_buyingRate.setText("$ " + String.format("%.2f", buyRate));
                text_buy_sellingRate.setText("$ " + String.format("%.2f", sellRate));
                BTMApplication.getInstance().setSellingRate(sellingRate);
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
            AlertMessage.showError(button_buyActivity_sellBitcoins, "Please complete Merhcnat Profit Threshold setup from settings");
            return false;
        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getHotWalletBenificiaryKey())) {
            AlertMessage.showError(button_buyActivity_sellBitcoins, "Please update hot wallet beneficiary from settings");
            return false;
        }
        return true;
    }
}
