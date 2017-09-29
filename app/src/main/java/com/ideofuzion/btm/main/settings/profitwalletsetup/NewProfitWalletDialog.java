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

public class NewProfitWalletDialog implements Response.Listener<JSONObject>, Response.ErrorListener, Constants.ResultCode {
    TextView text_minMaxBalance_header;
    EditText profitWalletUserUsername;
    EditText profitWalletUserPassword;
    Button button_minMaxBalance_submit;
    private Typeface fontBold;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private DialogHelper dialogHelper;
    Dialog dialog;

    public void show(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_new_profit_wallet);


        dialogHelper = new DialogHelper(context);
        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(context).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(context).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(context).getTypefaceBold();

        text_minMaxBalance_header = (TextView) dialog.findViewById(R.id.text_minMaxBalance_header);
        profitWalletUserUsername = (EditText) dialog.findViewById(R.id.edit_minMaxBalance_minBalance);
        profitWalletUserPassword = (EditText) dialog.findViewById(R.id.edit_minMaxBalance_maxBalance);
        button_minMaxBalance_submit = (Button) dialog.findViewById(R.id.button_minMaxBalance_submit);

        text_minMaxBalance_header.setTypeface(fontBold);
        profitWalletUserPassword.setTypeface(fontSemiBold);
        profitWalletUserUsername.setTypeface(fontSemiBold);
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
        updateTaglineParams.put("createNewProfitWallet", "1");
        updateTaglineParams.put("profitWalletUserName", profitWalletUserUsername.getText().toString());
        updateTaglineParams.put("profitWalletAddress", profitWalletUserPassword.getText().toString());

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }


    boolean validateFields() {
        if (profitWalletUserUsername.getText().toString().isEmpty()) {
            AlertMessage.showError(profitWalletUserPassword, "Please enter username");
            profitWalletUserUsername.requestFocus();
            return false;
        }
        if (profitWalletUserPassword.getText().toString().isEmpty()) {
            AlertMessage.showError(profitWalletUserPassword, "Please enter password");
            profitWalletUserPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        AlertMessage.showError(profitWalletUserPassword, Constants.ERROR_CHECK_INTERNET);

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
                    AlertMessage.show(profitWalletUserPassword, "Success");

                    if (!serverMessageResponse.getData().isEmpty()) {
                        Gson gsonForUser = new Gson();
                        BTMUser btmUser = gsonForUser.fromJson(serverMessageResponse.getData(), BTMUser.class);
                        BTMApplication.getInstance().setBTMUserObj(btmUser);
                    }
                    dialog.dismiss();
                } else {
                    AlertMessage.showError(profitWalletUserPassword, serverMessageResponse.getMessage());
                }//end oe else
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null

    }

}
