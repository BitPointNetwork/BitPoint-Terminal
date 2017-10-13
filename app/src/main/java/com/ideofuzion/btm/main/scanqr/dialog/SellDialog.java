package com.ideofuzion.btm.main.scanqr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.ideofuzion.btm.main.settings.SetupProfitMarginActivity;
import com.ideofuzion.btm.main.settings.SetupProfitThresholdActivity;
import com.ideofuzion.btm.main.transfercomplete.TransferCompleteActivity;
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

import static com.ideofuzion.btm.main.settings.PinCodeActivity.EXTRA_FROM_REGISTRATION;
import static com.ideofuzion.btm.utils.Constants.ResultCode.CODE_SUCCESS;

/**
 * Created by khali on 10/12/2017.
 */

public class SellDialog implements Response.Listener<JSONObject>, Response.ErrorListener {
    TextView header;
    EditText edit_sell_walletName;
    EditText edit_sell_walletPassword, edit_sell_amount;
    Button cancel;
    Button submit;
    Dialog dialog;
    Context context;
    private DialogHelper dialogHelper;
    String address;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private Typeface fontBold;


    public SellDialog(Context context, String address) {
        this.context = context;
        this.address = address;
    }

    public void show() {
        dialogHelper = new DialogHelper(context);
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sell);
        header = (TextView) dialog.findViewById(R.id.header);
        edit_sell_amount = (EditText) dialog.findViewById(R.id.edit_sell_amount);
        edit_sell_walletName = (EditText) dialog.findViewById(R.id.edit_sell_walletName);
        edit_sell_walletPassword = (EditText) dialog.findViewById(R.id.edit_sell_walletPassword);
        cancel = (Button) dialog.findViewById(R.id.cancel);
        submit = (Button) dialog.findViewById(R.id.submit);

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(context).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(context).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(context).getTypefaceBold();

        header.setTypeface(fontBold);
        edit_sell_amount.setTypeface(fontSemiBold);
        edit_sell_walletName.setTypeface(fontSemiBold);
        edit_sell_walletPassword.setTypeface(fontSemiBold);
        submit.setTypeface(fontSemiBold);
        cancel.setTypeface(fontSemiBold);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Internet.isConnected(context)) {
                    if (validateFields()) {
                        sendRequestToServer();
                    }
                } else {
                    AlertMessage.showError(edit_sell_walletName, Constants.ERROR_NO_INTERNET);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    boolean validateFields() {
        if (edit_sell_walletName.getText().toString().isEmpty()) {
            AlertMessage.showError(edit_sell_walletName, "Please enter wallet name");
            edit_sell_walletName.requestFocus();
            return false;
        }
        if (edit_sell_walletPassword.getText().toString().isEmpty()) {
            AlertMessage.showError(edit_sell_walletName, "Please enter wallet password");
            edit_sell_walletPassword.requestFocus();
            return false;
        }
        if (edit_sell_amount.getText().toString().isEmpty()) {
            AlertMessage.showError(edit_sell_amount, "Please enter BTC amount");
            edit_sell_amount.requestFocus();
            return false;
        }
        return true;
    }


    private void sendRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_RECEIVE_BALANCE;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("amount", edit_sell_amount.getText().toString());
        updateTaglineParams.put("walletName", edit_sell_walletName.getText().toString());
        updateTaglineParams.put("walletPassword", edit_sell_walletPassword.getText().toString());
        updateTaglineParams.put("merchantUserName", BTMApplication.getInstance().getBTMUserObj().getUserName());
        updateTaglineParams.put("customerAddress", address);

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }


    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        AlertMessage.showError(edit_sell_walletName, Constants.ERROR_CHECK_INTERNET);

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
                    context.startActivity(new Intent(context, TransferCompleteActivity.class)
                            .putExtra("transactionId", serverMessageResponse.getData().replace("{", "").replace("}", "").replaceAll(" ", ""))
                            .putExtra(TransferCompleteActivity.EXTRA_AMOUNT_TO_BE_GIVEN
                                    , String.valueOf(Double.parseDouble(BTMApplication.getInstance().getSellingRate())
                                            * Double.parseDouble(edit_sell_amount.getText().toString()))));
                    dialog.dismiss();
                    //redirectUserAfterSuccessSignIn(serverMessageResponse.getData(), null);
                } else {
                    AlertMessage.showError(edit_sell_walletName, serverMessageResponse.getMessage());
                }//end oe else
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null

    }
}
