package com.ideofuzion.btm.main.authorization;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.enteramount.EnterAmountActivity;
import com.ideofuzion.btm.model.ServerMessage;
import com.ideofuzion.btm.network.VolleyRequestHelper;
import com.ideofuzion.btm.utils.AlertMessage;
import com.ideofuzion.btm.utils.Constants;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.Internet;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.grantland.widget.AutofitTextView;

/**
 * Created by ideofuzion on 6/20/2017.
 *
 * this is an activity and pin authentication
 * is performed in this activity
 */

public class PinCodeAuthActivity extends Activity implements View.OnKeyListener, Constants.ResultCode, Response.Listener<JSONObject>,
        Response.ErrorListener {
    AutofitTextView text_pinCodeAuth_youKey, text_pinCodeAuth_dollarRate;
    TextView text_pinCodeAuth_title;
    EditText edit_pinCodeAuth_1, edit_pinCodeAuth_2, edit_pinCodeAuth_3, edit_pinCodeAuth_4;
    Button button_pinCodeAuth_cancel, button_pinCodeAuth_authorize;
    private boolean isBackedClicked = false;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private Typeface fontBold;
    String preCode = "";
    DialogHelper dialogHelper;
    String bitcoinAmount = "";
    double bitcoinsX,bitcoinY;

    /**
     * this function will be called each time
     * the activity starts and in this function we
     * set the ui of the activity and ui listener and perform
     * other tasks that we want to perform on activity start
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_confirm_authorization);

            initResources();

            initTypeface();

            addListener();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
        }
    }

    /**
     * init the fonts on ui elements
     */
    private void initTypeface() {
        text_pinCodeAuth_youKey.setTypeface(fontSemiBold);
        text_pinCodeAuth_dollarRate.setTypeface(fontSemiBold);
        text_pinCodeAuth_title.setTypeface(fontBold);
        edit_pinCodeAuth_1.setTypeface(fontSemiBold);
        edit_pinCodeAuth_2.setTypeface(fontSemiBold);
        edit_pinCodeAuth_3.setTypeface(fontSemiBold);
        edit_pinCodeAuth_4.setTypeface(fontSemiBold);
        button_pinCodeAuth_cancel.setTypeface(fontBold);
        button_pinCodeAuth_authorize.setTypeface(fontBold);
    }


    /**
     * adding listener to ui elements
     */
    private void addListener() {


        edit_pinCodeAuth_1.setOnKeyListener(this);
        edit_pinCodeAuth_2.setOnKeyListener(this);
        edit_pinCodeAuth_3.setOnKeyListener(this);
        edit_pinCodeAuth_4.setOnKeyListener(this);

        edit_pinCodeAuth_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(s.toString(), s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    isBackedClicked = false;
                    edit_pinCodeAuth_2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_pinCodeAuth_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    isBackedClicked = false;
                    edit_pinCodeAuth_3.requestFocus();
                } else if (s.length() == 0 && isBackedClicked) {
                    edit_pinCodeAuth_1.requestFocus();
                    isBackedClicked = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_pinCodeAuth_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    isBackedClicked = false;
                    edit_pinCodeAuth_4.requestFocus();
                } else if (s.length() == 0 && isBackedClicked) {
                    edit_pinCodeAuth_2.requestFocus();
                    isBackedClicked = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_pinCodeAuth_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    isBackedClicked = true;
                    edit_pinCodeAuth_3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_pinCodeAuth_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_pinCodeAuth_authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                authorize();
            }
        });

    }


    /**
     * this will verify if the any of the code field is empty and
     * if not it will send code verification request to server
     * and if field is empty it will toast a message
     *
     */
    private void authorize() {
        if (!edit_pinCodeAuth_1.getText().toString().isEmpty() &&
                !edit_pinCodeAuth_2.getText().toString().isEmpty() &&
                !edit_pinCodeAuth_3.getText().toString().isEmpty() &&
                !edit_pinCodeAuth_4.getText().toString().isEmpty()) {
            if (Internet.isConnected(PinCodeAuthActivity.this)) {
                preCode = edit_pinCodeAuth_1.getText().toString() +
                        edit_pinCodeAuth_2.getText().toString() +
                        edit_pinCodeAuth_3.getText().toString() +
                        edit_pinCodeAuth_4.getText().toString();

                resetAllFields();
                dialogHelper.showProgressDialog();
                sendPINCodeVerifyRequestToServer();
            } else {
                AlertMessage.showError(edit_pinCodeAuth_1, Constants.ERROR_NO_INTERNET);
            }
        } else {
            AlertMessage.showError(edit_pinCodeAuth_1, "Please enter PIN Code");
        }
    }


    /**
     * getting reference to xml elements
     * and setting some class objects to use later
     *
     */
    private void initResources() {

        //init dialog helper object
        dialogHelper = new DialogHelper(this);

        //getting values from intent if any
        if (getIntent().hasExtra(EnterAmountActivity.BITCOIN_AMOUNT_X)) {
            bitcoinsX = getIntent().getDoubleExtra(EnterAmountActivity.BITCOIN_AMOUNT_X,0);
            bitcoinY = getIntent().getDoubleExtra(EnterAmountActivity.BITCOIN_AMOUNT_Y,0);

        }

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(getApplicationContext()).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();


        //init ui elements
        text_pinCodeAuth_youKey = (AutofitTextView) findViewById(R.id.text_pinCodeAuth_yourKey);
        text_pinCodeAuth_dollarRate = (AutofitTextView) findViewById(R.id.text_pinCodeAuth_dollarRate);
        text_pinCodeAuth_title = (TextView) findViewById(R.id.text_pinCodeAuth_title);
        edit_pinCodeAuth_1 = (EditText) findViewById(R.id.edit_pinCodeAuth_1);
        edit_pinCodeAuth_2 = (EditText) findViewById(R.id.edit_pinCodeAuth_2);
        edit_pinCodeAuth_3 = (EditText) findViewById(R.id.edit_pinCodeAuth_3);
        edit_pinCodeAuth_4 = (EditText) findViewById(R.id.edit_pinCodeAuth_4);
        button_pinCodeAuth_cancel = (Button) findViewById(R.id.button_pinCodeAuth_cancel);
        button_pinCodeAuth_authorize = (Button) findViewById(R.id.button_pinCodeAuth_authorize);

        //setting up init data
        text_pinCodeAuth_youKey.setText(Html.fromHtml("<i>Your ID</i> " + BTMApplication.getInstance().getQrModel().getBitcoin()));
        text_pinCodeAuth_dollarRate.setText("1 BTC = " + BTMApplication.getInstance().getSellingRate() + Constants.CURRENCY);

        text_pinCodeAuth_title.setText(Html.fromHtml("<i>Enter PIN Code</i>"));
    }

    /**
     * this function will reset call fields to empty
     */
    private void resetAllFields() {
        edit_pinCodeAuth_1.setText("");
        edit_pinCodeAuth_2.setText("");
        edit_pinCodeAuth_3.setText("");
        edit_pinCodeAuth_4.setText("");
        edit_pinCodeAuth_1.requestFocus();
    }


    /**
     * this function will be called
     * when delete button is pressed if
     * the focus is on any of the edit texts
     *
     *
     * @param view
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        //for only action up
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (view.getId() == edit_pinCodeAuth_1.getId()) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edit_pinCodeAuth_1.setText("");
                    return true;
                }//end of del button
            } else if (view.getId() == edit_pinCodeAuth_2.getId()) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (edit_pinCodeAuth_2.getText().length() == 0) {
                        edit_pinCodeAuth_1.requestFocus();
                        edit_pinCodeAuth_1.setText("");
                    }//end of if for text length 0
                    else {
                        edit_pinCodeAuth_2.setText("");
                        return true;
                    }
                }
            } else if (view.getId() == edit_pinCodeAuth_3.getId()) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (edit_pinCodeAuth_3.getText().length() == 0) {
                        edit_pinCodeAuth_2.requestFocus();
                        edit_pinCodeAuth_2.setText("");
                    }//end of if for text length 0
                    else {
                        edit_pinCodeAuth_3.setText("");
                        return true;
                    }
                }
            } else if (view.getId() == edit_pinCodeAuth_4.getId()) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    authorize();
                } else if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (edit_pinCodeAuth_4.getText().length() == 0) {
                        edit_pinCodeAuth_3.requestFocus();
                        edit_pinCodeAuth_3.setText("");
                    }//end of if for text length 0
                    else {
                        edit_pinCodeAuth_4.setText("");
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * sending pin code verification
     * request to server
     */
    private void sendPINCodeVerifyRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_VERIFY_PASSCODE;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("merchantId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("UserPasscode", preCode);
        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);

    }

    /**
     this function will be called when the server throws an
     * error when failed to connect to server
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        AlertMessage.showError(edit_pinCodeAuth_1, Constants.ERROR_CHECK_INTERNET);

    }

    /**
     * this function will be called
     * if the server successfully executes
     * over pin verification request
     *
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        if (response != null) {
            ServerMessage serverMessageResponse = new ServerMessage();
            if (serverMessageResponse != null) {
                try {
                    if (response.has("data")) {
                        serverMessageResponse.setData(response.getString("data"));
                    }

                    serverMessageResponse.setCode(response.getInt("code"));
                    serverMessageResponse.setMessage(response.getString("message"));
                    if (serverMessageResponse.getCode() == CODE_SUCCESS) {
                        SendBitcoins sendBitcoins = new SendBitcoins(PinCodeAuthActivity.this, text_pinCodeAuth_dollarRate, bitcoinsX, bitcoinY);
                        sendBitcoins.sendBitcoinTransferRequestToServer();
                    } else {
                        AlertMessage.showError(edit_pinCodeAuth_1, serverMessageResponse.getMessage());
                    }//end oe else
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//end of if for server mesage not null
        }

    }
}
