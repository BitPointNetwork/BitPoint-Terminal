package com.ideofuzion.btm.main.enteramount;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.authorization.PinCodeAuthActivity;
import com.ideofuzion.btm.main.authorization.SendBitcoins;
import com.ideofuzion.btm.main.transfercomplete.TransferCompleteActivity;
import com.ideofuzion.btm.model.ServerMessage;
import com.ideofuzion.btm.network.VolleyRequestHelper;
import com.ideofuzion.btm.utils.AlertMessage;
import com.ideofuzion.btm.utils.Constants;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.MyUtils;
import com.ideofuzion.btm.utils.SessionManager;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import me.grantland.widget.AutofitTextView;

import static com.ideofuzion.btm.utils.Constants.ResultCode.CODE_SUCCESS;


/**
 * Created by khali on 6/2/2017.
 */

public class EnterAmountActivity extends Activity {
    Typeface fontRegular;
    Typeface fontSemiBold;
    Typeface fontBold;
    public static String BITCOIN_AMOUNT = "BITCOIN_AMOUNT";


    AutofitTextView text_enterAmountFragment_userKey, text_enterAmountFragment_dollarRate;
    TextView text_enterAmountFragment_bitcoinAmount;
    TextView text_enterAmountFragment_title,
            text_enterAmountFragment_description, text_enterAmountFragment_BTC;

    Button button_enterAmountFragment_cancel, button_enterAmountFragment_proceed;

    Button button_enterAmountFragment_1, button_enterAmountFragment_2, button_enterAmountFragment_3,
            button_enterAmountFragment_4, button_enterAmountFragment_5, button_enterAmountFragment_6,
            button_enterAmountFragment_7, button_enterAmountFragment_8, button_enterAmountFragment_9,
            button_enterAmountFragment_0, button_enterAmountFragment_clear;

    TextView edit_enterAmountFragment_calculator;


    Float dollarRate;
    DialogHelper dialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_enter_amount);

            initResources();

            initTypeface();

            addListener();

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void addListener() {

        button_enterAmountFragment_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_enterAmountFragment_calculator.append(0 + "");
            }
        });
        button_enterAmountFragment_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_enterAmountFragment_calculator.append(1 + "");
            }
        });
        button_enterAmountFragment_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_enterAmountFragment_calculator.append(2 + "");
            }
        });
        button_enterAmountFragment_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_enterAmountFragment_calculator.append(3 + "");
            }
        });
        button_enterAmountFragment_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_enterAmountFragment_calculator.append(4 + "");
            }
        });
        button_enterAmountFragment_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_enterAmountFragment_calculator.append(5 + "");
            }
        });
        button_enterAmountFragment_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_enterAmountFragment_calculator.append(6 + "");
            }
        });
        button_enterAmountFragment_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_enterAmountFragment_calculator.append(7 + "");
            }
        });
        button_enterAmountFragment_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_enterAmountFragment_calculator.append(8 + "");
            }
        });
        button_enterAmountFragment_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_enterAmountFragment_calculator.append(9 + "");
            }
        });
        button_enterAmountFragment_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_enterAmountFragment_calculator.setText("");
            }
        });

        button_enterAmountFragment_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!edit_enterAmountFragment_calculator.getText().toString().isEmpty()) {
                        if (Long.parseLong(edit_enterAmountFragment_calculator.getText().toString()) > 0) {

                            if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getEthereumUserPasscode()))
                                requestServerToSendBalance();
                            else {
                                startActivity(new Intent(EnterAmountActivity.this,
                                        PinCodeAuthActivity.class)
                                .putExtra(BITCOIN_AMOUNT,text_enterAmountFragment_bitcoinAmount.getText().toString()));
                            }
/*
                        startActivity(new Intent(EnterAmountActivity.this, AuthorizationActivity.class).putExtra(BITCOIN_AMOUNT, text_enterAmountFragment_bitcoinAmount.getText().toString()));
*/
                        } else {
                            AlertMessage.showError(edit_enterAmountFragment_calculator, "Please enter amount greater than zero");
                        }
                    } else {
                        AlertMessage.showError(edit_enterAmountFragment_calculator, "Amount should not be empty");
                    }
                } catch (Exception e) {

                    AlertMessage.showError(text_enterAmountFragment_bitcoinAmount, "Invalid Amount");
                }

            }
        });
        button_enterAmountFragment_cancel.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit_enterAmountFragment_calculator.addTextChangedListener(new

                                                                           TextWatcher() {
                                                                               @Override
                                                                               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                                               }

                                                                               @Override
                                                                               public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                                               }

                                                                               @Override
                                                                               public void afterTextChanged(Editable s) {
                                                                                   if (edit_enterAmountFragment_calculator.getText().toString().equals("")) {
                                                                                       text_enterAmountFragment_bitcoinAmount.setText("0.0");
                                                                                   } else {
                                                                                       Double bitcoins = Double.parseDouble(edit_enterAmountFragment_calculator.getText().toString().trim()) /
                                                                                               dollarRate;

                                                                                       text_enterAmountFragment_bitcoinAmount.setText(BigDecimal.valueOf(bitcoins).toPlainString());
                                                                                   }
                                                                               }
                                                                           });
    }


    private void initTypeface() {

        text_enterAmountFragment_userKey.setTypeface(fontSemiBold);
        text_enterAmountFragment_dollarRate.setTypeface(fontSemiBold);
        text_enterAmountFragment_title.setTypeface(fontBold);
        text_enterAmountFragment_description.setTypeface(fontSemiBold);
        text_enterAmountFragment_bitcoinAmount.setTypeface(fontSemiBold);
        // text_enterAmountFragment_BTC.setTypeface(fontSemiBold);
        button_enterAmountFragment_cancel.setTypeface(fontBold);
        button_enterAmountFragment_proceed.setTypeface(fontBold);
        button_enterAmountFragment_1.setTypeface(fontSemiBold);
        button_enterAmountFragment_2.setTypeface(fontSemiBold);
        button_enterAmountFragment_3.setTypeface(fontSemiBold);
        button_enterAmountFragment_4.setTypeface(fontSemiBold);
        button_enterAmountFragment_5.setTypeface(fontSemiBold);
        button_enterAmountFragment_6.setTypeface(fontSemiBold);
        button_enterAmountFragment_7.setTypeface(fontSemiBold);
        button_enterAmountFragment_8.setTypeface(fontSemiBold);
        button_enterAmountFragment_9.setTypeface(fontSemiBold);
        button_enterAmountFragment_0.setTypeface(fontSemiBold);
        button_enterAmountFragment_clear.setTypeface(fontSemiBold);
        edit_enterAmountFragment_calculator.setTypeface(fontBold);
    }

    private void initResources() {

        dialogHelper = new DialogHelper(this);
        dollarRate = Float.valueOf(BTMApplication.getInstance().getBTMUserObj().getBitcoinDollarRate());
        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(this).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(this).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(this).getTypefaceBold();


        dollarRate = Float.valueOf(BTMApplication.getInstance().getBTMUserObj().getBitcoinDollarRate());

        text_enterAmountFragment_userKey = (AutofitTextView) findViewById(R.id.text_enterAmountFragment_userKey);
        text_enterAmountFragment_dollarRate = (AutofitTextView) findViewById(R.id.text_enterAmountFragment_dollarRate);
        text_enterAmountFragment_title = (TextView) findViewById(R.id.text_enterAmountFragment_title);
        text_enterAmountFragment_description = (TextView) findViewById(R.id.text_enterAmountFragment_description);
        text_enterAmountFragment_bitcoinAmount = (TextView) findViewById(R.id.text_enterAmountFragment_bitcoinAmount);
        //text_enterAmountFragment_BTC = (TextView) findViewById(R.id.text_enterAmountFragment_BTC);

        edit_enterAmountFragment_calculator = (TextView) findViewById(R.id.edit_enterAmountFragment_calculator);

        button_enterAmountFragment_cancel = (Button) findViewById(R.id.button_enterAmountFragment_cancel);
        button_enterAmountFragment_proceed = (Button) findViewById(R.id.button_enterAmountFragment_proceed);

        button_enterAmountFragment_1 = (Button) findViewById(R.id.button_enterAmountFragment_1);
        button_enterAmountFragment_2 = (Button) findViewById(R.id.button_enterAmountFragment_2);
        button_enterAmountFragment_3 = (Button) findViewById(R.id.button_enterAmountFragment_3);
        button_enterAmountFragment_4 = (Button) findViewById(R.id.button_enterAmountFragment_4);
        button_enterAmountFragment_5 = (Button) findViewById(R.id.button_enterAmountFragment_5);
        button_enterAmountFragment_6 = (Button) findViewById(R.id.button_enterAmountFragment_6);
        button_enterAmountFragment_7 = (Button) findViewById(R.id.button_enterAmountFragment_7);
        button_enterAmountFragment_8 = (Button) findViewById(R.id.button_enterAmountFragment_8);
        button_enterAmountFragment_9 = (Button) findViewById(R.id.button_enterAmountFragment_9);
        button_enterAmountFragment_0 = (Button) findViewById(R.id.button_enterAmountFragment_0);
        button_enterAmountFragment_clear = (Button) findViewById(R.id.button_enterAmountFragment_clear);


        //init data
        text_enterAmountFragment_userKey.setText(Html.fromHtml("<i>Your ID:</i> " + BTMApplication.getInstance().getQrModel().getPublicBitcoinId()));
        text_enterAmountFragment_dollarRate.setText("1 BTC = " + dollarRate + " USD");
        text_enterAmountFragment_title.setText(Html.fromHtml("<i>Enter Amount</i>"));

    }


    private void requestServerToSendBalance() {
        SendBitcoins sendBitcoins = new SendBitcoins(this, edit_enterAmountFragment_calculator);
        sendBitcoins.sendBitcoinTransferRequestToServer(text_enterAmountFragment_bitcoinAmount.getText().toString());
    }
}
