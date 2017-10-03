package com.ideofuzion.btm.main.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
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
import com.google.gson.Gson;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.buy.BuyActivity;
import com.ideofuzion.btm.main.settings.profitwalletsetup.ExistingProfitWalletActivity;
import com.ideofuzion.btm.model.BTMUser;
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

/**
 * Created by khali on 6/19/2017.
 */

public class PinCodeActivity extends Activity implements View.OnKeyListener, Constants.ResultCode, Response.Listener<JSONObject>,
        Response.ErrorListener {
    public static final String EXTRA_FROM_REGISTRATION = "registration";
    TextView text_settings_header, text_pincode_title;
    EditText edit_pinCode_1, edit_pinCode_2, edit_pinCode_3, edit_pinCode_4;
    Button button_pinCode_cancel, button_pinCode_set;
    private boolean isBackedClicked = false;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private Typeface fontBold;
    String PINCode = "";
    public static final String NEW_PIN_CODE = "New PIN Code";
    public static final String OLD_PIN_CODE = "Enter Old PIN Code";
    DialogHelper dialogHelper;
    boolean isFromRegistration = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_pin_code);

            initResources();

            initTypeface();

            addListener();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTypeface() {
        text_settings_header.setTypeface(fontSemiBold);
        text_pincode_title.setTypeface(fontSemiBold);
        edit_pinCode_1.setTypeface(fontSemiBold);
        edit_pinCode_2.setTypeface(fontSemiBold);
        edit_pinCode_3.setTypeface(fontSemiBold);
        edit_pinCode_4.setTypeface(fontSemiBold);
        button_pinCode_cancel.setTypeface(fontBold);
        button_pinCode_set.setTypeface(fontBold);
    }

    private void addListener() {


        edit_pinCode_1.setOnKeyListener(this);
        edit_pinCode_2.setOnKeyListener(this);
        edit_pinCode_3.setOnKeyListener(this);
        edit_pinCode_4.setOnKeyListener(this);

        edit_pinCode_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(s.toString(), s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    isBackedClicked = false;
                    edit_pinCode_2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_pinCode_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    isBackedClicked = false;
                    edit_pinCode_3.requestFocus();
                } else if (s.length() == 0 && isBackedClicked) {
//                    edit_mobile_code_verification_code_1.requestFocus();
//                    isBackedClicked = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_pinCode_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    isBackedClicked = false;
                    edit_pinCode_4.requestFocus();
                } else if (s.length() == 0 && isBackedClicked) {
//                    edit_mobile_code_verification_code_2.requestFocus();
//                    isBackedClicked = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_pinCode_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
//                    isBackedClicked = true;
//                    edit_mobile_code_verification_code_3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_pinCode_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_pinCode_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePinCode();
            }
        });

    }

    private void updatePinCode() {
        if (!edit_pinCode_1.getText().toString().isEmpty() &&
                !edit_pinCode_2.getText().toString().isEmpty() &&
                !edit_pinCode_3.getText().toString().isEmpty() &&
                !edit_pinCode_4.getText().toString().isEmpty()) {
/*
                text_pinCode_title.setText(NEW_PIN_CODE);
*/
            PINCode = edit_pinCode_1.getText().toString() +
                    edit_pinCode_2.getText().toString() +
                    edit_pinCode_3.getText().toString() +
                    edit_pinCode_4.getText().toString();

            if (text_pincode_title.getText().toString().equals(OLD_PIN_CODE)) {
                if (PINCode.equals(BTMApplication.getInstance().getBTMUserObj().getEthereumUserPasscode())) {
                    resetAllFields();
                    text_pincode_title.setText(NEW_PIN_CODE);
                    button_pinCode_set.setText("Set");

                } else {
                    AlertMessage.showError(edit_pinCode_1, "PIN code doesn't matches");
                }
            } else {
                sendPINCodeAddRequestToServer();
            }
        } else {
            AlertMessage.showError(edit_pinCode_1, "Please enter PIN Code");
        }
    }

    private void initResources() {

        dialogHelper = new DialogHelper(this);

        isFromRegistration = getIntent().getBooleanExtra(EXTRA_FROM_REGISTRATION, false);

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(getApplicationContext()).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();


        text_settings_header = (TextView) findViewById(R.id.text_settings_header);
        edit_pinCode_1 = (EditText) findViewById(R.id.edit_pinCode_1);
        edit_pinCode_2 = (EditText) findViewById(R.id.edit_pinCode_2);
        edit_pinCode_3 = (EditText) findViewById(R.id.edit_pinCode_3);
        edit_pinCode_4 = (EditText) findViewById(R.id.edit_pinCode_4);
        button_pinCode_cancel = (Button) findViewById(R.id.button_pinCode_cancel);
        button_pinCode_set = (Button) findViewById(R.id.button_pinCode_set);
        text_pincode_title = (TextView) findViewById(R.id.text_pincode_title);


        if (isFromRegistration) {
            button_pinCode_cancel.setVisibility(View.GONE);
            text_pincode_title.setText(NEW_PIN_CODE);
            button_pinCode_set.setText("Set");

        } else {
            if (BTMApplication.getInstance().getBTMUserObj().getEthereumUserPasscode() != null) {
                text_pincode_title.setText(OLD_PIN_CODE);
                button_pinCode_set.setText("Proceed");

            } else {
                text_pincode_title.setText(NEW_PIN_CODE);
                button_pinCode_set.setText("Set");

            }

        }
    }

    private void resetAllFields() {
        edit_pinCode_1.setText("");
        edit_pinCode_2.setText("");
        edit_pinCode_3.setText("");
        edit_pinCode_4.setText("");
        edit_pinCode_1.requestFocus();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        //for only action up
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (view.getId() == edit_pinCode_1.getId()) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edit_pinCode_1.setText("");
                    return true;
                }//end of del button
            } else if (view.getId() == edit_pinCode_2.getId()) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (edit_pinCode_2.getText().length() == 0) {
                        edit_pinCode_1.requestFocus();
                        edit_pinCode_1.setText("");
                    }//end of if for text length 0
                    else {
                        edit_pinCode_2.setText("");
                        return true;
                    }
                }
            } else if (view.getId() == edit_pinCode_3.getId()) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (edit_pinCode_3.getText().length() == 0) {
                        edit_pinCode_2.requestFocus();
                        edit_pinCode_2.setText("");
                    }//end of if for text length 0
                    else {
                        edit_pinCode_3.setText("");
                        return true;
                    }
                }
            } else if (view.getId() == edit_pinCode_4.getId()) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    updatePinCode();
                } else if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (edit_pinCode_4.getText().length() == 0) {
                        edit_pinCode_3.requestFocus();
                        edit_pinCode_3.setText("");
                    }//end of if for text length 0
                    else {
                        edit_pinCode_4.setText("");
                        return true;
                    }
                }
            }
        }
        return false;
    }
/*
    private void sendPINCodeUpdateRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_UPDATE_PASSCODE;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("userName", BTMApplication.getInstance().getBTMUserObj().getBTMUserName());
        updateTaglineParams.put("oldPasscode", oldPINCode);
        updateTaglineParams.put("newPasscode", newPINCode);
        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);

    }*/

    private void sendPINCodeAddRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_ADD_PASSCODE;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("merchantId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("UserPasscode", PINCode);
        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        AlertMessage.showError(edit_pinCode_1, Constants.ERROR_CHECK_INTERNET);

    }

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
                        redirectUserAfterSuccessPINCodeUpdate(serverMessageResponse.getData());
                    } else {
                        AlertMessage.showError(edit_pinCode_1, serverMessageResponse.getMessage());
                    }//end oe else
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//end of if for server mesage not null
        }

    }

    private void redirectUserAfterSuccessPINCodeUpdate(String data) {
        if (!data.isEmpty()) {
            try {
                AlertMessage.show(edit_pinCode_1, "Successfully Updated");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                Gson gsonForUser = new Gson();
                BTMUser btmUser = gsonForUser.fromJson(data, BTMUser.class);
                BTMApplication.getInstance().setBTMUserObj(btmUser);
                if (isFromRegistration) {
                    startActivity(new Intent(PinCodeActivity.this,
                            MinMaxBalanceActivity.class)
                            .putExtra(PinCodeActivity.EXTRA_FROM_REGISTRATION, true));
                } else {
                    finish();
                }
            } catch (Exception e) {
            }
        }
    }
}
