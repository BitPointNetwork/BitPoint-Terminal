package com.ideofuzion.btm.main.authorization;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.enteramount.EnterAmountActivity;
import com.ideofuzion.btm.utils.Fonts;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;

/**
 * Created by khali on 6/2/2017.
 */

public class AuthorizationActivity extends Activity {

    Typeface fontRegular;
    Typeface fontSemiBold;
    Typeface fontBold;

    TextView text_authorizeFragment_userKey, text_authorizeFragment_dollarRate;
    CircleImageView imageView_authorizeFragment_profile;
    AutofitTextView text_authorizeFragment_name, text_authorizeFragment_emailTitle, text_authorizeFragment_email,
            text_authorizeFragment_mobileNumberTitle, text_authorizeFragment_mobileNumber,
            text_authorizeFragment_addressTitle, text_authorizeFragment_address, text_authorizeFragment_bitcoinAmount;
    TextView
            text_authorizeFragment_BTC, text_authorizeFragment_description;
    Button button_authorizeFragment_cancel, button_authorizeFragment_authorize;
    private String dollarRate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_authorize);

            initResources();

            initTypefaces();

            addListener();
        }catch (Exception e){}
    }


    private void addListener() {
        button_authorizeFragment_authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BTMApplication.getInstance().getBTMUserObj().getPasscodeStatus() == 100) {
                    SendBitcoins sendBitcoins = new SendBitcoins(AuthorizationActivity.this,text_authorizeFragment_userKey);
                    sendBitcoins.sendBitcoinTransferRequestToServer(text_authorizeFragment_bitcoinAmount.getText().toString(),
                            BTMApplication.getInstance().getBitpointUser().getBitcoinPublicKey());
                } else {
                    startActivity(new Intent(AuthorizationActivity.this, PinCodeAuthActivity.class)
                            .putExtra(EnterAmountActivity.BITCOIN_AMOUNT,text_authorizeFragment_bitcoinAmount.getText().toString()));
                }
            }
        });
        button_authorizeFragment_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTypefaces() {
        text_authorizeFragment_userKey.setTypeface(fontSemiBold);
        text_authorizeFragment_dollarRate.setTypeface(fontSemiBold);
        text_authorizeFragment_name.setTypeface(fontBold);
        text_authorizeFragment_emailTitle.setTypeface(fontSemiBold);
        text_authorizeFragment_email.setTypeface(fontSemiBold);
        text_authorizeFragment_mobileNumberTitle.setTypeface(fontSemiBold);
        text_authorizeFragment_mobileNumber.setTypeface(fontSemiBold);
        text_authorizeFragment_addressTitle.setTypeface(fontSemiBold);
        text_authorizeFragment_address.setTypeface(fontSemiBold);
        text_authorizeFragment_bitcoinAmount.setTypeface(fontSemiBold);
        text_authorizeFragment_BTC.setTypeface(fontSemiBold);
        text_authorizeFragment_description.setTypeface(fontSemiBold);
        button_authorizeFragment_cancel.setTypeface(fontBold);
        button_authorizeFragment_authorize.setTypeface(fontBold);
    }

    private void initResources() {

        dollarRate = BTMApplication.getInstance().getBTMUserObj().getBitcoinDollarRate();

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(getApplicationContext()).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();


        text_authorizeFragment_userKey = (TextView) findViewById(R.id.text_authorizeFragment_userKey);
        text_authorizeFragment_dollarRate = (TextView) findViewById(R.id.text_authorizeFragment_dollarRate);
        imageView_authorizeFragment_profile = (CircleImageView) findViewById(R.id.imageView_authorizeFragment_profile);

        text_authorizeFragment_name = (AutofitTextView) findViewById(R.id.text_authorizeFragment_name);
        text_authorizeFragment_emailTitle = (AutofitTextView) findViewById(R.id.text_authorizeFragment_emailTitle);
        text_authorizeFragment_email = (AutofitTextView) findViewById(R.id.text_authorizeFragment_email);
        text_authorizeFragment_mobileNumberTitle = (AutofitTextView) findViewById(R.id.text_authorizeFragment_mobileNumberTitle);
        text_authorizeFragment_mobileNumber = (AutofitTextView) findViewById(R.id.text_authorizeFragment_mobileNumber);
        text_authorizeFragment_addressTitle = (AutofitTextView) findViewById(R.id.text_authorizeFragment_addressTitle);
        text_authorizeFragment_address = (AutofitTextView) findViewById(R.id.text_authorizeFragment_address);
        text_authorizeFragment_bitcoinAmount = (AutofitTextView) findViewById(R.id.text_authorizeFragment_bitcoinAmount);
        text_authorizeFragment_BTC = (TextView) findViewById(R.id.text_authorizeFragment_BTC);
        text_authorizeFragment_description = (TextView) findViewById(R.id.text_authorizeFragment_description);

        button_authorizeFragment_cancel = (Button) findViewById(R.id.button_authorizeFragment_cancel);
        button_authorizeFragment_authorize = (Button) findViewById(R.id.button_authorizeFragment_authorize);

        // init data
        if (!BTMApplication.getInstance().getBitpointUser().getProfilePicUrl().isEmpty())
            Picasso.with(this).load(BTMApplication.getInstance().getBitpointUser().getProfilePicUrl()).placeholder(getResources().getDrawable(R.drawable.img_placeholder)).into(imageView_authorizeFragment_profile);
        text_authorizeFragment_userKey.setText(Html.fromHtml("<i>Your ID</i> " + BTMApplication.getInstance().getBitpointUser().getBitcoinPublicKey()));
        text_authorizeFragment_dollarRate.setText("1 BTC = " + dollarRate + " USD");
        text_authorizeFragment_name.setText(BTMApplication.getInstance().getBitpointUser().getFullName());
        text_authorizeFragment_email.setText(BTMApplication.getInstance().getBitpointUser().getEmailAddress());
        text_authorizeFragment_address.setText(BTMApplication.getInstance().getBitpointUser().getAddresss());
        text_authorizeFragment_mobileNumber.setText(BTMApplication.getInstance().getBitpointUser().getMobileNum());
        if (getIntent().hasExtra(EnterAmountActivity.BITCOIN_AMOUNT)) {
            text_authorizeFragment_bitcoinAmount.setText(getIntent().getStringExtra(EnterAmountActivity.BITCOIN_AMOUNT));
        }

    }
}
