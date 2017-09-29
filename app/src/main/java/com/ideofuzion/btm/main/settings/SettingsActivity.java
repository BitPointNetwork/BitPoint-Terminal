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
import com.ideofuzion.btm.main.settings.profitwalletsetup.ProfitWalletOptionDialog;
import com.ideofuzion.btm.utils.Fonts;

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
    LinearLayout linearLayout_settings_profitSetup;
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

    private void adListener() {
        /*linearLayout_settings_pinCodeSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, PinCodeActivity.class));
            }
        });
        linearLayout_settings_setupBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MinMaxBalanceActivity().initResources(SettingsActivity.this);
            }
        });
        linearLayout_settings_krackenSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KrakenSetupActivity().initResources(SettingsActivity.this);
            }
        });
        linearLayout_settings_SetupProfitWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProfitWalletOptionDialog().initResources(SettingsActivity.this);
            }
        });
        linearLayout_settings_profitSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetupProfitActivity().initResources(SettingsActivity.this);
            }
        });
        linearLayout_settings_bitpointProfitWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BitpointProfitWalletActivity().initResources(SettingsActivity.this);
            }
        });
        linearLayout_settings_merchantProfitThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetupProfitThresholdActivity().initResources(SettingsActivity.this);
            }
        });
        linearLayout_settings_hotWalletBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateHotWalletBeneficiaryDialog().initResources(SettingsActivity.this);
            }
        });*/
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
        linearLayout_settings_profitSetup = (LinearLayout) findViewById(R.id.linearLayout_settings_profitSetup);
        text_settings_profitSetup = (TextView) findViewById(R.id.text_settings_profitSetup);
        text_settings_dollarRate.setText("1 BTC = " + BTMApplication.getInstance().getBTMUserObj().getBitcoinDollarRate() + " USD");
        linearLayout_settings_bitpointProfitWallet = (LinearLayout) findViewById(R.id.linearLayout_settings_bitpointProfitWallet);
        text_settings_bitpointProfitWallet = (TextView) findViewById(R.id.text_settings_bitpointProfitWallet);
        linearLayout_settings_merchantProfitThreshold = (LinearLayout) findViewById(R.id.linearLayout_settings_merchantProfitThreshold);
        text_settings_merchantProfitThreshold = (TextView) findViewById(R.id.text_settings_merchantProfitThreshold);
        linearLayout_settings_hotWalletBeneficiary = (LinearLayout) findViewById(R.id.linearLayout_settings_hotWalletBeneficiary);
        text_settings_hotWalletBeneficiary = (TextView) findViewById(R.id.text_settings_hotWalletBeneficiary);
    }

}
