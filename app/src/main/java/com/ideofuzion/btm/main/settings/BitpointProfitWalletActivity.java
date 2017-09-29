package com.ideofuzion.btm.main.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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

/**
 * Created by khali on 9/23/2017.
 */

public class BitpointProfitWalletActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener, Constants.ResultCode {
    TextView text_minMaxBalance_header;
    EditText bitpointProfitWalletKrakenBenificiaryKey;
    EditText bitpointProfitWalletAddress;
    Button button_minMaxBalance_submit;
    private Typeface fontBold;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private DialogHelper dialogHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitpoint_profit_wallet);

        initResources();
    }

    public void initResources() {


        dialogHelper = new DialogHelper(this);
        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(this).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(this).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(this).getTypefaceBold();

        text_minMaxBalance_header = (TextView) findViewById(R.id.text_minMaxBalance_header);
        bitpointProfitWalletKrakenBenificiaryKey = (EditText) findViewById(R.id.bitpointProfitWalletKrakenBenificiaryKey);
        bitpointProfitWalletAddress = (EditText) findViewById(R.id.bitpointProfitWalletAddress);
        button_minMaxBalance_submit = (Button) findViewById(R.id.button_minMaxBalance_submit);

        text_minMaxBalance_header.setTypeface(fontBold);
        bitpointProfitWalletAddress.setTypeface(fontSemiBold);
        bitpointProfitWalletKrakenBenificiaryKey.setTypeface(fontSemiBold);
        button_minMaxBalance_submit.setTypeface(fontBold);

        button_minMaxBalance_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BitpointProfitWalletActivity.this,SetupProfitActivity.class));
             /*   if (validateFields()) {
                    sendRequestToServer();
                }*/
            }
        });

    }

    private void sendRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_CREATE_BITPOINT_PROFIT_WALLET;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("merchantId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("bitpointProfitWalletKrakenBenificiaryKey", bitpointProfitWalletKrakenBenificiaryKey.getText().toString());
        updateTaglineParams.put("bitpointProfitWalletAddress", bitpointProfitWalletAddress.getText().toString());

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }


    boolean validateFields() {
        if (bitpointProfitWalletAddress.getText().toString().isEmpty()) {
            AlertMessage.showError(bitpointProfitWalletAddress, "Please enter bitpoint Profit Wallet Address");
            bitpointProfitWalletAddress.requestFocus();
            return false;
        }
        if (bitpointProfitWalletKrakenBenificiaryKey.getText().toString().isEmpty()) {
            AlertMessage.showError(bitpointProfitWalletAddress, "Please enter bitpoint Profit Wallet Kraken Benificiary Key");
            bitpointProfitWalletKrakenBenificiaryKey.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        AlertMessage.showError(bitpointProfitWalletAddress, Constants.ERROR_CHECK_INTERNET);

    }

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
                    AlertMessage.show(bitpointProfitWalletAddress, "Success");

                    if (!serverMessageResponse.getData().isEmpty()) {
                        Gson gsonForUser = new Gson();
                        BTMUser btmUser = gsonForUser.fromJson(serverMessageResponse.getData(), BTMUser.class);
                        BTMApplication.getInstance().setBTMUserObj(btmUser);
                    }
                } else {
                    AlertMessage.showError(bitpointProfitWalletAddress, serverMessageResponse.getMessage());
                }//end oe else
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null

    }

}
