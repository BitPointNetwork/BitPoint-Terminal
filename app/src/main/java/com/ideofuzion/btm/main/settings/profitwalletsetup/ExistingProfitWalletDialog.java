package com.ideofuzion.btm.main.settings.profitwalletsetup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
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

public class ExistingProfitWalletDialog implements Response.Listener<JSONObject>, Response.ErrorListener, Constants.ResultCode {
    TextView text_minMaxBalance_header;
    EditText profitWalletKrakenBenificiaryKey;
    EditText profitWalletAddress;
    Button button_minMaxBalance_submit;
    private Typeface fontBold;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private DialogHelper dialogHelper;
    Dialog dialog;

    public void show(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exisitng_profit_wallet);


        dialogHelper = new DialogHelper(context);
        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(context).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(context).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(context).getTypefaceBold();

        text_minMaxBalance_header = (TextView) dialog.findViewById(R.id.text_minMaxBalance_header);
        profitWalletKrakenBenificiaryKey = (EditText) dialog.findViewById(R.id.edit_minMaxBalance_minBalance);
        profitWalletAddress = (EditText) dialog.findViewById(R.id.edit_minMaxBalance_maxBalance);
        button_minMaxBalance_submit = (Button) dialog.findViewById(R.id.button_minMaxBalance_submit);

        text_minMaxBalance_header.setTypeface(fontBold);
        profitWalletAddress.setTypeface(fontSemiBold);
        profitWalletKrakenBenificiaryKey.setTypeface(fontSemiBold);
        button_minMaxBalance_submit.setTypeface(fontBold);

        button_minMaxBalance_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    sendRequestToServer();
                }
            }
        });

        dialog.show();
    }

    private void sendRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_CREATE_PROFIT_WALLET;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("merchantId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("createNewProfitWallet", "0");
        updateTaglineParams.put("profitWalletKrakenBenificiaryKey", profitWalletKrakenBenificiaryKey.getText().toString());
        updateTaglineParams.put("profitWalletAddress", profitWalletAddress.getText().toString());

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }


    boolean validateFields() {
        if (profitWalletKrakenBenificiaryKey.getText().toString().isEmpty()) {
            AlertMessage.showError(profitWalletAddress, "Please enter Profit Wallet Kraken Benificiary Key");
            profitWalletKrakenBenificiaryKey.requestFocus();
            return false;
        }
        if (profitWalletAddress.getText().toString().isEmpty()) {
            AlertMessage.showError(profitWalletAddress, "Please enter Profit Wallet Address");
            profitWalletAddress.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        AlertMessage.showError(profitWalletAddress, Constants.ERROR_CHECK_INTERNET);

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
                    AlertMessage.show(profitWalletAddress, "Success");

                    if (!serverMessageResponse.getData().isEmpty()) {
                        Gson gsonForUser = new Gson();
                        BTMUser btmUser = gsonForUser.fromJson(serverMessageResponse.getData(), BTMUser.class);
                        BTMApplication.getInstance().setBTMUserObj(btmUser);
                    }
                    dialog.dismiss();
                } else {
                    AlertMessage.showError(profitWalletAddress, serverMessageResponse.getMessage());
                }//end oe else
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null

    }

}
