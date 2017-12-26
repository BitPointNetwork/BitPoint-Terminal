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
import android.widget.ImageView;
import android.widget.TextView;

import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.authorization.PinCodeAuthActivity;
import com.ideofuzion.btm.main.authorization.SendBitcoins;
import com.ideofuzion.btm.utils.AlertMessage;
import com.ideofuzion.btm.utils.Constants;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.MyUtils;

import java.math.BigDecimal;

import me.grantland.widget.AutofitTextView;


/**
 * Created by ideofuzion on 6/2/2017.
 *
 * this is an activity user is provided with the on screen calculator
 * in this activity wher user can enter the amount of bitcoint he/she
 * want to buy and user can enter amount both in bitcoin and pounds
 */

public class EnterAmountActivity extends Activity {
    public static final String BITCOIN_AMOUNT_Y = "BITCOIN_AMOUNT_Y";
    Typeface fontRegular;
    Typeface fontSemiBold;
    Typeface fontBold;
    public static String BITCOIN_AMOUNT = "BITCOIN_AMOUNT";
    Double bitcoinsX;
    Double bitcoinY;


    AutofitTextView text_enterAmountFragment_userKey, text_enterAmountFragment_dollarRate;
    TextView text_enterAmountFragment_bitcoinAmount, text_enterAmount_currencySymbol;
    TextView text_enterAmountFragment_title,
            text_enterAmountFragment_description, text_enterAmountFragment_BTC;

    Button button_enterAmountFragment_cancel, button_enterAmountFragment_proceed;

    Button button_enterAmountFragment_1, button_enterAmountFragment_2, button_enterAmountFragment_3,
            button_enterAmountFragment_4, button_enterAmountFragment_5, button_enterAmountFragment_6,
            button_enterAmountFragment_7, button_enterAmountFragment_8, button_enterAmountFragment_9,
            button_enterAmountFragment_0, button_enterAmountFragment_clear;

    TextView edit_enterAmountFragment_calculator;
    ImageView imageView_enterAmountFragment_switch;


    Double dollarRate;
    DialogHelper dialogHelper;
    public static final String BITCOIN_AMOUNT_X = "BITCOIN_AMOUNT_X";

    public static final String BTC = "btc";
    public static final String POUND = "pound";
    String activeCurrency = POUND;

    /**
     * this function will  be called each time activity starts and
     * all init setup is done here that is required to start the activity
     *
     * @param savedInstanceState
     */
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


    /**
     * adding listener to ui elements
     */

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
                String amount = edit_enterAmountFragment_calculator.getText().toString();
                if (amount.length() > 0)
                    edit_enterAmountFragment_calculator.setText(
                            amount.substring(0, amount.length() - 1));
            }
        });
        button_enterAmountFragment_clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                edit_enterAmountFragment_calculator.setText("");
                return false;
            }
        });

        imageView_enterAmountFragment_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView_enterAmountFragment_switch.getDrawable().getConstantState()
                        .equals(getResources().getDrawable(R.drawable.conv_btc).getConstantState())) {
                    imageView_enterAmountFragment_switch.setImageDrawable(getResources().getDrawable(R.drawable.conv_pound));
                    text_enterAmountFragment_description.setText("BTC will be transferred to your wallet");
                    text_enterAmount_currencySymbol.setText(Constants.BITCOIN_SYMBOL);

                    activeCurrency = POUND;
                    edit_enterAmountFragment_calculator.setText(edit_enterAmountFragment_calculator.getText().toString());
                } else {
                    imageView_enterAmountFragment_switch.setImageDrawable(getResources().getDrawable(R.drawable.conv_btc));
                    text_enterAmountFragment_description.setText("Pounds worth BTC will be transferred to your wallet");
                    text_enterAmount_currencySymbol.setText(Constants.POUND_SYMBOL);

                    activeCurrency = BTC;
                    edit_enterAmountFragment_calculator.setText(edit_enterAmountFragment_calculator.getText().toString());
                }
            }
        });

        button_enterAmountFragment_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!edit_enterAmountFragment_calculator.getText().toString().isEmpty()) {
                        if (Long.parseLong(edit_enterAmountFragment_calculator.getText().toString()) > 0) {
                            edit_enterAmountFragment_calculator.setText("");
                            if (MyUtils.isNullOrEmpty(BTMApplication.getInstance().getBTMUserObj().getEthereumUserPasscode()))
                                requestServerToSendBalance();
                            else {
                                startActivity(new Intent(EnterAmountActivity.this,
                                        PinCodeAuthActivity.class)
                                        .putExtra(BITCOIN_AMOUNT_X, bitcoinsX)
                                        .putExtra(BITCOIN_AMOUNT_Y, bitcoinY));
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

        edit_enterAmountFragment_calculator.addTextChangedListener(new TextWatcher() {
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
                    if (activeCurrency.equals(POUND)) {
                        Double rate = Double.valueOf(BTMApplication.getInstance().getOriginalSellingRate());
                        Double amountEntered = Double.valueOf(edit_enterAmountFragment_calculator.getText().toString().trim());
                        Double profitMargin = (amountEntered * Double.parseDouble(BTMApplication.getInstance().getBTMUserObj().getMerchantProfitMargin())) / 100;
                        Double amountMinusProfitMargin = amountEntered - profitMargin;

                        bitcoinsX = (amountMinusProfitMargin) /
                                rate;
                        bitcoinY = profitMargin / rate;

                        text_enterAmountFragment_bitcoinAmount.setText(MyUtils.getDecimalFormattedAmount(BigDecimal.valueOf(bitcoinsX).toPlainString()));
                    } else {
                        Double rate = Double.valueOf(BTMApplication.getInstance().getOriginalSellingRate());
                        Double amountEntered = Double.valueOf(text_enterAmountFragment_bitcoinAmount.getText().toString().trim());
                        Double profitMargin = (amountEntered * Double.parseDouble(BTMApplication.getInstance().getBTMUserObj().getMerchantProfitMargin())) / 100;
                        Double amountMinusProfitMargin = amountEntered - profitMargin;

                        bitcoinsX = (amountMinusProfitMargin) /
                                rate;
                        bitcoinY = profitMargin / rate;
                        String amount = edit_enterAmountFragment_calculator.getText().toString().trim();
                        text_enterAmountFragment_bitcoinAmount.setText(MyUtils.getDecimalFormattedAmount(String.valueOf(Double.parseDouble(amount) * dollarRate)));
                    }
                }
            }
        });
    }

    /**
     * applying fonts to ui elements
     */
    private void initTypeface() {

        text_enterAmountFragment_userKey.setTypeface(fontSemiBold);
        text_enterAmountFragment_dollarRate.setTypeface(fontSemiBold);
        text_enterAmountFragment_title.setTypeface(fontBold);
        text_enterAmountFragment_description.setTypeface(fontSemiBold);
        text_enterAmountFragment_bitcoinAmount.setTypeface(fontSemiBold);
        text_enterAmount_currencySymbol.setTypeface(fontSemiBold);
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

    /**
     * getting reference to ui element of xml
     * init different objects that are required later
     * like dialogHelpers and other font objects
     * init data at startup
     */
    private void initResources() {
        //init dialog helper obj
        dialogHelper = new DialogHelper(this);

        //init dollar rate obj
        dollarRate = Double.valueOf(BTMApplication.getInstance().getSellingRate());

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(this).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(this).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(this).getTypefaceBold();


        //init ui resources
        text_enterAmount_currencySymbol = (TextView) findViewById(R.id.text_enterAmount_currencySymbol);
        imageView_enterAmountFragment_switch = (ImageView) findViewById(R.id.imageView_enterAmountFragment_switch);
        imageView_enterAmountFragment_switch.setImageDrawable(getResources().getDrawable(R.drawable.conv_pound));
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
        text_enterAmountFragment_description.setText("BTC will be transferred to your wallet");
        text_enterAmount_currencySymbol.setText(Constants.BITCOIN_SYMBOL);
        text_enterAmountFragment_bitcoinAmount.setText("0.0");
        text_enterAmountFragment_userKey.setText(Html.fromHtml("<i>Your ID:</i> " + BTMApplication.getInstance().getQrModel().getBitcoin()));
        text_enterAmountFragment_dollarRate.setText("1 BTC = " + MyUtils.getDecimalFormattedAmount(dollarRate.toString()) + " " + Constants.CURRENCY);
        text_enterAmountFragment_title.setText(Html.fromHtml("<i>Enter Amount</i>"));

    }


    /**
     * requesting server to send balance
     */
    private void requestServerToSendBalance() {
        SendBitcoins sendBitcoins = new SendBitcoins(this, edit_enterAmountFragment_calculator, bitcoinsX, bitcoinY);
        sendBitcoins.sendBitcoinTransferRequestToServer();
    }
}
