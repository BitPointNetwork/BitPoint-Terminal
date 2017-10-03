package com.ideofuzion.btm.main.settings;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.settings.profitwalletsetup.ProfitWalletOptionActivity;
import com.ideofuzion.btm.utils.AlertMessage;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.MyUtils;

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
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getProfitWalletAddress())) {
            linearLayout_settings_SetupProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));

        } else {
            linearLayout_settings_SetupProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));

        }
        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getProfitWalletKrakenBenificiaryKey())) {
            linearLayout_settings_SetupProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));

        } else {
            linearLayout_settings_SetupProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.completed_settings_background));

        }

        if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getBitpointProfitWalletAddress())) {
            linearLayout_settings_bitpointProfitWallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.incomplete_settings_background));

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
                startActivity(new Intent(SettingsActivity.this, ProfitWalletOptionActivity.class));

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
    }

    private void initResources() {


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
        text_settings_dollarRate.setText("1 BTC = " + BTMApplication.getInstance().getBTMUserObj().getBitcoinDollarRate() + " USD");
        linearLayout_settings_bitpointProfitWallet = (LinearLayout) findViewById(R.id.linearLayout_settings_bitpointProfitWallet);
        text_settings_bitpointProfitWallet = (TextView) findViewById(R.id.text_settings_bitpointProfitWallet);
        linearLayout_settings_merchantProfitThreshold = (LinearLayout) findViewById(R.id.linearLayout_settings_merchantProfitThreshold);
        text_settings_merchantProfitThreshold = (TextView) findViewById(R.id.text_settings_merchantProfitThreshold);
        linearLayout_settings_hotWalletBeneficiary = (LinearLayout) findViewById(R.id.linearLayout_settings_hotWalletBeneficiary);
        text_settings_hotWalletBeneficiary = (TextView) findViewById(R.id.text_settings_hotWalletBeneficiary);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            validateFields();

        }catch (Exception e)
        {}
    }
}
