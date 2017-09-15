package com.ideofuzion.btm.main.settings;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.utils.Fonts;

public class SettingsActivity extends AppCompatActivity {
    LinearLayout linearLayout_settings_tagline,linearLayout_settings_pinCodeSetup;
    TextView text_settings_tagline,text_settings_pinCodeSetup,text_settings_header,text_settings_dollarRate;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private Typeface fontBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_settings);

            initResources();

            initTypefaces();

            adListener();
        }catch (Exception e)
        {}
    }

    private void adListener() {
        linearLayout_settings_tagline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,TagLineActivity.class));
            }
        });
        linearLayout_settings_pinCodeSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,PinCodeActivity.class));
            }
        });
    }

    private void initTypefaces() {
        text_settings_tagline.setTypeface(fontSemiBold);
        text_settings_pinCodeSetup.setTypeface(fontSemiBold);
        text_settings_header.setTypeface(fontSemiBold);
        text_settings_dollarRate.setTypeface(fontSemiBold);

    }

    private void initResources() {


        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(getApplicationContext()).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();

        linearLayout_settings_tagline = (LinearLayout) findViewById(R.id.linearLayout_settings_tagline);
        linearLayout_settings_pinCodeSetup = (LinearLayout) findViewById(R.id.linearLayout_settings_pinCodeSetup);
        text_settings_tagline = (TextView) findViewById(R.id.text_settings_tagline);
        text_settings_pinCodeSetup = (TextView) findViewById(R.id.text_settings_pinCodeSetup);
        text_settings_header = (TextView) findViewById(R.id.text_settings_header);
        text_settings_dollarRate = (TextView) findViewById(R.id.text_settings_dollarRate);
        text_settings_dollarRate.setText("1 BTC = " + BTMApplication.getInstance().getBTMUserObj().getBitcoinDollarRate() + " USD");

    }

}
