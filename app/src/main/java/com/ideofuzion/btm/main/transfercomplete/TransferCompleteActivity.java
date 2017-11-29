package com.ideofuzion.btm.main.transfercomplete;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.buy.BuyActivity;
import com.ideofuzion.btm.utils.Constants;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.MyUtils;

import me.grantland.widget.AutofitTextView;

/**
 * Created by khali on 6/2/2017.
 */

public class TransferCompleteActivity extends Activity {

    public static final String EXTRA_AMOUNT_TO_BE_GIVEN = "amountToBeGiven";
    Typeface fontRegular;
    Typeface fontSemiBold;
    Typeface fontBold;

    AutofitTextView text_transferCompleteFragment_userKey, text_transferCompleteFragment_dollarRate;
    TextView text_transferCompleteFragment_transactionComplete, text_transferCompleteFragment_amount;
    ImageView imageView_transferCompleteFragment_success;
    Button button_transferCompleteFragment_ok;
    private String dollarRate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.fragment_transfer_complete);

            initResources(savedInstanceState);

            initTypefaces();

            addListener();

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
            e.printStackTrace();
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
        text_transferCompleteFragment_amount.setTypeface(fontSemiBold);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("transactionId", getIntent().getStringExtra("transactionId"));
        if (getIntent().hasExtra(EXTRA_AMOUNT_TO_BE_GIVEN)) {
            outState.putString(EXTRA_AMOUNT_TO_BE_GIVEN, getIntent().getStringExtra(EXTRA_AMOUNT_TO_BE_GIVEN).toString());
            outState.putString("senderAddress", getIntent().getStringExtra("senderAddress").toString());
        }
    }

    private void initResources(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            if (!savedInstanceState.containsKey(EXTRA_AMOUNT_TO_BE_GIVEN))
                showTarnsacionId(savedInstanceState.getString("transactionId"), false);
        } else {
            if (!getIntent().hasExtra(EXTRA_AMOUNT_TO_BE_GIVEN))
                showTarnsacionId(getIntent().getStringExtra("transactionId"), false);
        }

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(getApplicationContext()).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();

        text_transferCompleteFragment_userKey = (AutofitTextView) findViewById(R.id.text_transferCompleteFragment_userKey);
        text_transferCompleteFragment_dollarRate = (AutofitTextView) findViewById(R.id.text_transferCompleteFragment_dollarRate);
        text_transferCompleteFragment_transactionComplete = (TextView) findViewById(R.id.text_transferCompleteFragment_transactionComplete);
        imageView_transferCompleteFragment_success = (ImageView) findViewById(R.id.imageView_transferCompleteFragment_success);
        button_transferCompleteFragment_ok = (Button) findViewById(R.id.button_transferCompleteFragment_ok);
        text_transferCompleteFragment_amount = (TextView) findViewById(R.id.text_transferCompleteFragment_amount);
        try {
            text_transferCompleteFragment_userKey.setText(Html.fromHtml("<i>Your ID:</i> " + BTMApplication.getInstance().getQrModel().getBitcoin()));
        } catch (Exception r) {
        }
        //init data
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_AMOUNT_TO_BE_GIVEN)) {
                dollarRate = BTMApplication.getInstance().getBuyingRate();

                text_transferCompleteFragment_amount.setText("£ " + Double.parseDouble(savedInstanceState.getString(EXTRA_AMOUNT_TO_BE_GIVEN))*Double.parseDouble(dollarRate)
                        + " you will receive for your Bitcoin");
                text_transferCompleteFragment_userKey.setText(Html.fromHtml("<i>Your ID:</i> " + savedInstanceState.getString("senderAddress")));

            } else
                dollarRate = BTMApplication.getInstance().getSellingRate();
        } else {
            if (getIntent().hasExtra(EXTRA_AMOUNT_TO_BE_GIVEN)) {
                dollarRate = BTMApplication.getInstance().getBuyingRate();

                text_transferCompleteFragment_amount.setText("£ " + Double.parseDouble(getIntent().getStringExtra(EXTRA_AMOUNT_TO_BE_GIVEN))*Double.parseDouble(dollarRate)
                        + " you will receive for your Bitcoin");
                text_transferCompleteFragment_userKey.setText(Html.fromHtml("<i>Your ID:</i> " + getIntent().getStringExtra("senderAddress")));

            } else
                dollarRate = BTMApplication.getInstance().getSellingRate();
        }

        text_transferCompleteFragment_dollarRate.setText("1 BTC = " + MyUtils.getDecimalFormattedAmount(dollarRate) + " " + Constants.CURRENCY);


    }

    private void showTarnsacionId(String id, boolean b) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_transaction_id);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView transactionId = (TextView) dialog.findViewById(R.id.transactionId);
        TextView ok = (TextView) dialog.findViewById(R.id.ok);

        if (b) {
            title.setText("Transaction Id\n(Tap id to copy)");
        } else {
            title.setText("Transaction Id\nPhotograph this transaction Id and keep it for reference");
        }

        title.setTypeface(fontBold);
        transactionId.setTypeface(fontSemiBold);
        ok.setTypeface(fontSemiBold);


        transactionId.setText(id);

        transactionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Transaction Id", transactionId.getText().toString());
                Toast.makeText(TransferCompleteActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Transaction Id", transactionId.getText().toString());
                clipboard.setPrimaryClip(clip);
                dialog.dismiss();
            }
        });

        dialog.show();


    }
}
