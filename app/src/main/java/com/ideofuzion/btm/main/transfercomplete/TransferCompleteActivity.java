package com.ideofuzion.btm.main.transfercomplete;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.buy.BuyActivity;
import com.ideofuzion.btm.utils.Fonts;

import org.w3c.dom.Text;

import me.grantland.widget.AutofitTextView;

/**
 * Created by khali on 6/2/2017.
 */

public class TransferCompleteActivity extends Activity {

    Typeface fontRegular;
    Typeface fontSemiBold;
    Typeface fontBold;

    AutofitTextView text_transferCompleteFragment_userKey, text_transferCompleteFragment_dollarRate;
    TextView text_transferCompleteFragment_transactionComplete;
    ImageView imageView_transferCompleteFragment_success;
    Button button_transferCompleteFragment_ok;
    private String dollarRate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.fragment_transfer_complete);

            initResources();

            initTypefaces();

            addListener();

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
        }
    }


    private void addListener() {
        button_transferCompleteFragment_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransferCompleteActivity.this, BuyActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    private void initTypefaces() {
        text_transferCompleteFragment_userKey.setTypeface(fontBold);
        text_transferCompleteFragment_dollarRate.setTypeface(fontSemiBold);
        text_transferCompleteFragment_transactionComplete.setTypeface(fontBold);
        button_transferCompleteFragment_ok.setTypeface(fontBold);
    }

    private void initResources() {

        dollarRate = BTMApplication.getInstance().getBTMUserObj().getBitcoinDollarRate();

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(getApplicationContext()).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();

        text_transferCompleteFragment_userKey = (AutofitTextView) findViewById(R.id.text_transferCompleteFragment_userKey);
        text_transferCompleteFragment_dollarRate = (AutofitTextView) findViewById(R.id.text_transferCompleteFragment_dollarRate);
        text_transferCompleteFragment_transactionComplete = (TextView) findViewById(R.id.text_transferCompleteFragment_transactionComplete);
        imageView_transferCompleteFragment_success = (ImageView) findViewById(R.id.imageView_transferCompleteFragment_success);
        button_transferCompleteFragment_ok = (Button) findViewById(R.id.button_transferCompleteFragment_ok);


        //init data
        text_transferCompleteFragment_userKey.setText(Html.fromHtml("<i>Your ID:</i> " + BTMApplication.getInstance().getQrModel().getPublicBitcoinId()));
        text_transferCompleteFragment_dollarRate.setText("1 BTC = " + dollarRate + " USD");
    }
}
