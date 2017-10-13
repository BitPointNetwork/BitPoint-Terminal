package com.ideofuzion.btm.main.transfercomplete;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.buy.BuyActivity;
import com.ideofuzion.btm.main.settings.UpdateHotWalletBeneficiaryActivity;
import com.ideofuzion.btm.utils.Fonts;

import org.w3c.dom.Text;

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

            initResources();

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

    private void initResources() {

        showTarnsacionId(getIntent().getStringExtra("transactionId"));



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
        text_transferCompleteFragment_amount = (TextView) findViewById(R.id.text_transferCompleteFragment_amount);

        //init data
        if (getIntent().getStringExtra(EXTRA_AMOUNT_TO_BE_GIVEN) != null) {
            text_transferCompleteFragment_amount.setText("You have to pay $ " + getIntent().getStringExtra(EXTRA_AMOUNT_TO_BE_GIVEN)
                    + " to customer");
        }
        text_transferCompleteFragment_userKey.setText(Html.fromHtml("<i>Your ID:</i> " + BTMApplication.getInstance().getQrModel().getPublicBitcoinId()));
        text_transferCompleteFragment_dollarRate.setText("1 BTC = " + dollarRate + " USD");
    }

    private void showTarnsacionId(String id) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_transaction_id);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView transactionId = (TextView) dialog.findViewById(R.id.transactionId);
        TextView ok = (TextView) dialog.findViewById(R.id.ok);

        title.setTypeface(fontBold);
        transactionId.setTypeface(fontSemiBold);
        ok.setTypeface(fontSemiBold);

        title.setText("Transaction Id\n(Tap id to copy)");


        transactionId.setText(id);

        transactionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Transaction Id", transactionId.getText().toString());
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
                Toast.makeText(TransferCompleteActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();


    }
}
