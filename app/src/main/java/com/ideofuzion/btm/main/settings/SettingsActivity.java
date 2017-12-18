package com.ideofuzion.btm.main.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.login.LoginActivity;
import com.ideofuzion.btm.main.settings.profitwalletsetup.ExistingProfitWalletActivity;
import com.ideofuzion.btm.main.settings.profitwalletsetup.ProfitWalletOptionActivity;
import com.ideofuzion.btm.model.BTMUser;
import com.ideofuzion.btm.model.ServerMessage;
import com.ideofuzion.btm.network.VolleyRequestHelper;
import com.ideofuzion.btm.utils.AlertMessage;
import com.ideofuzion.btm.utils.Constants;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.MyUtils;
import com.ideofuzion.btm.utils.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ideofuzion.btm.main.settings.PinCodeActivity.EXTRA_FROM_REGISTRATION;
import static com.ideofuzion.btm.utils.Constants.ResultCode.CODE_SUCCESS;

public class SettingsActivity extends AppCompatActivity {
    TextView text_settings_header, text_settings_dollarRate;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private Typeface fontBold;

    LinearLayout linearLayout_settings_pinCodeSetup;
    TextView text_settings_pinCodeSetup;
    LinearLayout linearLayout_settings_setupBalance;
    TextView text_settings_setupBalance;
    LinearLayout linearLayout_settings_krackenSetup;
    TextView text_settings_krackenSetup;
    LinearLayout linearLayout_settings_SetupProfitWallet;
    TextView text_settings_beneficiaryKey;
    LinearLayout linearLayout_settings_profitMarginSetup;
    TextView text_settings_profitSetup;

    LinearLayout linearLayout_settings_bitpointProfitWallet;
    TextView text_settings_bitpointProfitWallet;
    LinearLayout linearLayout_settings_merchantProfitThreshold;
    TextView text_settings_merchantProfitThreshold;
    LinearLayout linearLayout_settings_hotWalletBeneficiary;
    TextView text_settings_hotWalletBeneficiary;
    LinearLayout linearLayout_settings_bitcoinPublicAddress;
    TextView text_settings_bitcoinPublicAddress;
    LinearLayout linearLayout_settings_toggleexhchange;
    TextView text_settings_toggleexhchange;

    LinearLayout linearLayout_settings_logout;
    TextView text_settings_logout;
    private DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_settings);

            initResources();

            initTypefaces();

            adListener();

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validateFields() {
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getEthereumUserPasscode())) {
            linearLayout_settings_pinCodeSetup.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));
        } else {
            linearLayout_settings_pinCodeSetup.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));

        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getKrakenAPIKey())) {
            linearLayout_settings_krackenSetup.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));
        } else {
            linearLayout_settings_krackenSetup.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));
        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getKrakenAPISecret())) {
            linearLayout_settings_krackenSetup.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));

        } else {
            linearLayout_settings_krackenSetup.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));
        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getProfitWalletAddress()) &&
                MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getProfitWalletKrakenBenificiaryKey())) {
            linearLayout_settings_SetupProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));

        } else if (BTMApplication.getInstance().getBTMUserObj().getProfitWalletAddress().length() > 0 &&
                MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getProfitWalletKrakenBenificiaryKey())) {
            linearLayout_settings_SetupProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.half_complete_settings_background));

        } else {
            linearLayout_settings_SetupProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));

        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getProfitWalletKrakenBenificiaryKey())) {
            linearLayout_settings_SetupProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));

        } else {
            linearLayout_settings_SetupProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));

        }

        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getBitpointProfitWalletAddress()) && MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getBitpointProfitWalletKrakenBenificiaryKey())) {
            linearLayout_settings_bitpointProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));

        } else if (BTMApplication.getInstance().getBTMUserObj().getBitpointProfitWalletAddress().toString().length() > 0 && MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getBitpointProfitWalletKrakenBenificiaryKey())) {
            linearLayout_settings_bitpointProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.half_complete_settings_background));
        } else {
            linearLayout_settings_bitpointProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));

        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getMerchantProfitThreshold())) {
            linearLayout_settings_merchantProfitThreshold.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));

        } else {
            linearLayout_settings_merchantProfitThreshold.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));

        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getHotWalletBenificiaryKey())) {
            linearLayout_settings_hotWalletBeneficiary.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));

        } else {
            linearLayout_settings_hotWalletBeneficiary.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));

        }
        if (!BTMApplication.getInstance().getBTMUserObj().getExchangeStatus()) {
            linearLayout_settings_toggleexhchange.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));

        } else {
            linearLayout_settings_toggleexhchange.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));

        }

    }

    private void adListener() {
        linearLayout_settings_pinCodeSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, PinCodeActivity.class));
            }
        });
        linearLayout_settings_setupBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MinMaxBalanceActivity.class));
            }
        });
        linearLayout_settings_krackenSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, KrakenSetupActivity.class));
            }
        });
        linearLayout_settings_SetupProfitWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(SettingsActivity.this, ProfitWalletOptionActivity.class));
                startActivity(new Intent(SettingsActivity.this, ExistingProfitWalletActivity.class));
            }
        });
        linearLayout_settings_profitMarginSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, SetupProfitMarginActivity.class));

            }
        });
        linearLayout_settings_bitpointProfitWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, BitpointProfitWalletActivity.class));
            }
        });
        linearLayout_settings_merchantProfitThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, SetupProfitThresholdActivity.class));
            }
        });
        linearLayout_settings_hotWalletBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, UpdateHotWalletBeneficiaryActivity.class));

            }
        });
        linearLayout_settings_bitcoinPublicAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BTMApplication.getInstance().getBTMUserObj().getEthereumUserPasscode() != null) {
                    startActivity(new Intent(SettingsActivity.this, PinCodeActivity.class)
                            .putExtra(PinCodeActivity.EXTRA_SHOW_BITCOIN_PUBLIC_ADDRESS, true));
                } else {
                    new AlertDialog.Builder(SettingsActivity.this)
                            .setTitle("Your Bitcoin Address Key")
                            .setMessage(BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId())
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });
        linearLayout_settings_toggleexhchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestToSkipKraken();

            }
        });
        linearLayout_settings_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.getInstance(getApplicationContext()).createSession("", "");
                finish();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
    }

    private void initTypefaces() {
        text_settings_setupBalance.setTypeface(fontSemiBold);
        text_settings_krackenSetup.setTypeface(fontSemiBold);
        text_settings_beneficiaryKey.setTypeface(fontSemiBold);
        text_settings_profitSetup.setTypeface(fontSemiBold);
        text_settings_pinCodeSetup.setTypeface(fontSemiBold);
        text_settings_header.setTypeface(fontSemiBold);
        text_settings_dollarRate.setTypeface(fontSemiBold);
        text_settings_bitpointProfitWallet.setTypeface(fontSemiBold);
        text_settings_merchantProfitThreshold.setTypeface(fontSemiBold);
        text_settings_hotWalletBeneficiary.setTypeface(fontSemiBold);
        text_settings_bitcoinPublicAddress.setTypeface(fontSemiBold);
        text_settings_toggleexhchange.setTypeface(fontSemiBold);
        text_settings_logout.setTypeface(fontSemiBold);
    }

    private void initResources() {


        dialogHelper = new DialogHelper(this);
        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(this).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(this).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(this).getTypefaceBold();

        linearLayout_settings_pinCodeSetup = (LinearLayout) findViewById(R.id.linearLayout_settings_pinCodeSetup);
        text_settings_krackenSetup = (TextView) findViewById(R.id.text_settings_krackenSetup);
        text_settings_pinCodeSetup = (TextView) findViewById(R.id.text_settings_pinCodeSetup);
        text_settings_header = (TextView) findViewById(R.id.text_settings_header);
        text_settings_dollarRate = (TextView) findViewById(R.id.text_settings_dollarRate);
        linearLayout_settings_setupBalance = (LinearLayout) findViewById(R.id.linearLayout_settings_setupBalance);
        text_settings_setupBalance = (TextView) findViewById(R.id.text_settings_setupBalance);
        linearLayout_settings_krackenSetup = (LinearLayout) findViewById(R.id.linearLayout_settings_krackenSetup);
        linearLayout_settings_SetupProfitWallet = (LinearLayout) findViewById(R.id.linearLayout_settings_SetupProfitWallet);
        text_settings_beneficiaryKey = (TextView) findViewById(R.id.text_settings_beneficiaryKey);
        linearLayout_settings_profitMarginSetup = (LinearLayout) findViewById(R.id.linearLayout_settings_profitSetup);
        text_settings_profitSetup = (TextView) findViewById(R.id.text_settings_profitSetup);
        text_settings_dollarRate.setText("");
        linearLayout_settings_bitpointProfitWallet = (LinearLayout) findViewById(R.id.linearLayout_settings_bitpointProfitWallet);
        text_settings_bitpointProfitWallet = (TextView) findViewById(R.id.text_settings_bitpointProfitWallet);
        linearLayout_settings_merchantProfitThreshold = (LinearLayout) findViewById(R.id.linearLayout_settings_merchantProfitThreshold);
        text_settings_merchantProfitThreshold = (TextView) findViewById(R.id.text_settings_merchantProfitThreshold);
        linearLayout_settings_hotWalletBeneficiary = (LinearLayout) findViewById(R.id.linearLayout_settings_hotWalletBeneficiary);
        text_settings_hotWalletBeneficiary = (TextView) findViewById(R.id.text_settings_hotWalletBeneficiary);
        linearLayout_settings_bitcoinPublicAddress = (LinearLayout) findViewById(R.id.linearLayout_settings_bitcoinPublicAddress);
        text_settings_bitcoinPublicAddress = (TextView) findViewById(R.id.text_settings_bitcoinPublicAddress);
        linearLayout_settings_toggleexhchange = (LinearLayout) findViewById(R.id.linearLayout_settings_toggleexhchange);
        text_settings_toggleexhchange = (TextView) findViewById(R.id.text_settings_toggleexhchange);
        linearLayout_settings_logout = (LinearLayout) findViewById(R.id.linearLayout_settings_logout);
        text_settings_logout = (TextView) findViewById(R.id.text_settings_logout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            validateFields();

        } catch (Exception e) {
        }
    }

    private void sendRequestToSkipKraken() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_UPDATE_USE_KRAKEN;

        int toggleNotification = 1;
        if (BTMApplication.getInstance().getBTMUserObj().getExchangeStatus()) {
            toggleNotification = 0;
        } else {
            toggleNotification = 1;
        }

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("merchantId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("useKraken", String.valueOf(toggleNotification));

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, new ExchangeToggle());
        dialogHelper.showProgressDialog();
    }


    class ExchangeToggle implements Response.Listener<JSONObject>, Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            if (dialogHelper != null) {
                dialogHelper.hideProgressDialog();
            }
            Toast.makeText(SettingsActivity.this, "Please Try Agian", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResponse(JSONObject response) {
            if (dialogHelper != null) {
                dialogHelper.hideProgressDialog();
            }
            if (response != null) {
                ServerMessage serverMessageResponse = new ServerMessage();
                try {
                    serverMessageResponse.setData(response.getString("data"));
                    serverMessageResponse.setCode(response.getInt("code"));
                    serverMessageResponse.setMessage(response.getString("message"));
                    if (serverMessageResponse.getCode() == CODE_SUCCESS) {
                        if (!serverMessageResponse.getData().isEmpty()) {
                            Gson gsonForUser = new Gson();
                            BTMUser btmUser = gsonForUser.fromJson(serverMessageResponse.getData(), BTMUser.class);
                            BTMApplication.getInstance().setBTMUserObj(btmUser);
                            if (!BTMApplication.getInstance().getBTMUserObj().getExchangeStatus()) {
                                linearLayout_settings_toggleexhchange.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));

                            } else {
                                linearLayout_settings_toggleexhchange.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));

                            }

                        }
                    } else {
                        Toast.makeText(SettingsActivity.this, serverMessageResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }//end oe else
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }//en

        }
    }
}
