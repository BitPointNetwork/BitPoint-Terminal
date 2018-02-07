package com.ideofuzion.btm.main.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.ideofuzion.btm.model.BTMUser;
import com.ideofuzion.btm.model.ServerMessage;
import com.ideofuzion.btm.network.VolleyRequestHelper;
import com.ideofuzion.btm.utils.AlertMessage;
import com.ideofuzion.btm.utils.Constants;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ideofuzion.btm.main.settings.PinCodeActivity.EXTRA_FROM_REGISTRATION;

/**
 * Created by ideofuzion on 9/23/2017.
 *
 * this activity is used to setip min max balance of application by providing form
 * where user can input min and max balance and then the request is send to
 * server
 */

public class MinMaxBalanceActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener, Constants.ResultCode {
    TextView text_minMaxBalance_header;
    EditText edit_minMaxBalance_minBalance;
    EditText edit_minMaxBalance_maxBalance;
    Button button_minMaxBalance_submit;
    private Typeface fontBold;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private DialogHelper dialogHelper;
    private boolean isFromRegistration = false;
    private Button cancel;

    /**
     * this function will be called each time the activity starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_min_max_balance);

            initResources();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }catch (Exception e)
        {}
    }
    /**
     * getting data from intent,
     * initializing dialog helper object,
     * initiating fonts object and other ui resources, applying font to those ui resources
     * and applying click listener as well
     */
    public void initResources() {

        isFromRegistration = getIntent().getBooleanExtra(EXTRA_FROM_REGISTRATION, false);


        dialogHelper = new DialogHelper(this);

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(this).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(this).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(this).getTypefaceBold();

        text_minMaxBalance_header = (TextView) findViewById(R.id.text_minMaxBalance_header);
        edit_minMaxBalance_minBalance = (EditText) findViewById(R.id.edit_minMaxBalance_minBalance);
        edit_minMaxBalance_maxBalance = (EditText) findViewById(R.id.edit_minMaxBalance_maxBalance);
        button_minMaxBalance_submit = (Button) findViewById(R.id.button_minMaxBalance_submit);

        text_minMaxBalance_header.setTypeface(fontBold);
        edit_minMaxBalance_maxBalance.setTypeface(fontSemiBold);
        edit_minMaxBalance_minBalance.setTypeface(fontSemiBold);
        button_minMaxBalance_submit.setTypeface(fontBold);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromRegistration) {
                    startActivity(new Intent(MinMaxBalanceActivity.this,
                            KrakenSetupActivity.class).putExtra(EXTRA_FROM_REGISTRATION, true));
                } else {
                    finish();
                }
            }
        });
        cancel.setTypeface(fontBold);
        button_minMaxBalance_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateFields()) {
                    sendRequestToServer();
                }
            }
        });


        //init data
        edit_minMaxBalance_minBalance.setText(BTMApplication.getInstance().getBTMUserObj().getMinimumHotWalletBalance());
        edit_minMaxBalance_maxBalance.setText(BTMApplication.getInstance().getBTMUserObj().getMaximumHotWalletBalance());

    }

    /**
     * sending min max setup request to server
     */
    private void sendRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_UPDATE_MIN_MAX_BALANCE;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("merchantId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("minimumHotWalletBalance", edit_minMaxBalance_minBalance.getText().toString());
        updateTaglineParams.put("maximumHotWalletBalance", edit_minMaxBalance_maxBalance.getText().toString());

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }


    /**
     * validating edit text fields
     * @return
     */
    boolean validateFields() {
        if (edit_minMaxBalance_minBalance.getText().toString().isEmpty()) {
            AlertMessage.showError(edit_minMaxBalance_maxBalance, "Please enter min balance value");
            edit_minMaxBalance_minBalance.requestFocus();
            return false;
        }
        if (edit_minMaxBalance_maxBalance.getText().toString().isEmpty()) {
            AlertMessage.showError(edit_minMaxBalance_maxBalance, "Please enter max balance value");
            edit_minMaxBalance_maxBalance.requestFocus();
            return false;
        }
        return true;
    }



    /**
     * this function will be called when the server throws an
     * error when failed to connect to server
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        AlertMessage.showError(edit_minMaxBalance_maxBalance, Constants.ERROR_CHECK_INTERNET);

    }

    /**
     * this function will be called when
     * server successfully executes min mac balance setup request
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        if (response != null) {
            ServerMessage serverMessageResponse = new ServerMessage();
            try {
                serverMessageResponse.setData(response.getString("data"));
                serverMessageResponse.setCode(response.getInt("code"));
                serverMessageResponse.setMessage(response.getString("message"));
                if (serverMessageResponse.getCode() == CODE_SUCCESS) {
                    AlertMessage.show(edit_minMaxBalance_maxBalance, "Success");

                    if (!serverMessageResponse.getData().isEmpty()) {
                        Gson gsonForUser = new Gson();
                        BTMUser btmUser = gsonForUser.fromJson(serverMessageResponse.getData(), BTMUser.class);
                        BTMApplication.getInstance().setBTMUserObj(btmUser);
                        if (isFromRegistration) {
                            startActivity(new Intent(MinMaxBalanceActivity.this,
                                    KrakenSetupActivity.class).putExtra(EXTRA_FROM_REGISTRATION, true));
                        } else {
                            finish();
                        }
                    }
                } else {
                    AlertMessage.showError(edit_minMaxBalance_maxBalance, serverMessageResponse.getMessage());
                }//end oe else
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null

    }

}
