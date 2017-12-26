package com.ideofuzion.btm.main.settings;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.network.VolleyRequestHelper;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.MyUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class YourAddressAndBalanceActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    TextView text_yourBalance_header;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private Typeface fontBold;
    TextView text_yourBalance_yourBalanceText;
    TextView text_yourBalance_yourBalanceValue;
    TextView text_yourBalance_yourAddressText;
    TextView text_yourBalance_yourAddressValue;
    TextView text_yourBalance_copy;
    Button cancel;
    DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_your_adress_and_balance);
            dialogHelper = new DialogHelper(this);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            //init ui elements
            text_yourBalance_header = (TextView) findViewById(R.id.text_yourBalance_header);
            text_yourBalance_yourBalanceText = (TextView) findViewById(R.id.text_yourBalance_yourBalanceText);
            text_yourBalance_yourBalanceValue = (TextView) findViewById(R.id.text_yourBalance_yourBalanceValue);
            text_yourBalance_yourAddressText = (TextView) findViewById(R.id.text_yourBalance_yourAddressText);
            text_yourBalance_yourAddressValue = (TextView) findViewById(R.id.text_yourBalance_yourAddressValue);
            text_yourBalance_copy = (TextView) findViewById(R.id.text_yourBalance_copy);
            cancel = (Button) findViewById(R.id.cancel);
            //initializing TypeFaces objects
            fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();
            fontSemiBold = Fonts.getInstance(getApplicationContext()).getTypefaceSemiBold();
            fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();

            //init
            text_yourBalance_header.setTypeface(fontSemiBold);
            text_yourBalance_yourBalanceText.setTypeface(fontSemiBold);
            text_yourBalance_yourBalanceValue.setTypeface(fontSemiBold);
            text_yourBalance_yourAddressText.setTypeface(fontSemiBold);
            text_yourBalance_yourAddressValue.setTypeface(fontSemiBold);
            text_yourBalance_copy.setTypeface(fontSemiBold);
            cancel.setTypeface(fontRegular);

            //init data
            text_yourBalance_yourAddressValue.setText(BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId());

            //get usd to pound
            getUSDToPOUND();

            //adding click listener
            text_yourBalance_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Bitcoin Address",
                            BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(YourAddressAndBalanceActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUSDToPOUND() {
        String url = "https://blockchain.info/ticker";
        VolleyRequestHelper.getRequestWithParams(url, new HashMap<String, String>(), this);
        dialogHelper.showProgressDialog();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        dialogHelper.hideProgressDialog();
        if (BTMApplication.getInstance().getBTMUserObj().getHotWalletBalance() == null ||
                BTMApplication.getInstance().getBTMUserObj().getHotWalletBalance().equals("")) {
            text_yourBalance_yourBalanceValue.setText("0 BTC");

        } else
            text_yourBalance_yourBalanceValue.setText(BTMApplication.getInstance().getBTMUserObj().getHotWalletBalance() + " BTC");
    }

    @Override
    public void onResponse(JSONObject response) {
        dialogHelper.hideProgressDialog();
        try {
            if (BTMApplication.getInstance().getBTMUserObj().getHotWalletBalance() == null ||
                    BTMApplication.getInstance().getBTMUserObj().getHotWalletBalance().equals("")) {
                text_yourBalance_yourBalanceValue.setText("0 £");
            } else {
                Double balance = Double.parseDouble(BTMApplication.getInstance().getBTMUserObj().getHotWalletBalance()) *
                        new JSONObject(response.getString("GBP")).getDouble("last");
                text_yourBalance_yourBalanceValue.setText(BTMApplication.getInstance().getBTMUserObj().getHotWalletBalance() + " BTC\n" + MyUtils.getDecimalFormattedAmount(String.valueOf(balance)) + " £");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
