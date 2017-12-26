package com.ideofuzion.btm.main.settings;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.settings.profitwalletsetup.ProfitWalletOptionActivity;
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
 */

public class UpdateHotWalletBeneficiaryActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener, Constants.ResultCode {
    TextView text_minMaxBalance_header;
    EditText hotWalletBeneficiary;
    Button submit;
    private Typeface fontBold;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private DialogHelper dialogHelper;
    private boolean isFromRegistration = false;
    Button cancel;
    TextView textView_hotWalletAddressText;
    TextView textView_hotWalletAddressValue;
    TextView text_copy;


    /**
     * this function will be called each time the activity starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_update_hot_wallet_beneficiary);
            initResources();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
        }
    }

    /**
     * getting data from intent, init classes
     * objects including dialog helper, fonts
     * and applying those fonts to ui resources
     * and applying click listeners to ui resources
     */
    public void initResources() {


        isFromRegistration = getIntent().getBooleanExtra(EXTRA_FROM_REGISTRATION, false);

        dialogHelper = new DialogHelper(this);
        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(this).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(this).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(this).getTypefaceBold();

        text_minMaxBalance_header = (TextView) findViewById(R.id.text_minMaxBalance_header);
        hotWalletBeneficiary = (EditText) findViewById(R.id.edit_minMaxBalance_minBalance);
        textView_hotWalletAddressText = (TextView) findViewById(R.id.textView_hotWalletAddressText);
        textView_hotWalletAddressValue = (TextView) findViewById(R.id.textView_hotWalletAddressValue);
        text_copy = (TextView) findViewById(R.id.text_copy);
        submit = (Button) findViewById(R.id.button_minMaxBalance_submit);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    finish();
            }
        });
        if (isFromRegistration) {
            cancel.setVisibility(View.GONE);
        } else {
            cancel.setVisibility(View.VISIBLE);
        }
        cancel.setTypeface(fontBold);

        text_minMaxBalance_header.setTypeface(fontBold);
        hotWalletBeneficiary.setTypeface(fontSemiBold);
        submit.setTypeface(fontBold);
        textView_hotWalletAddressText.setTypeface(fontSemiBold);
        textView_hotWalletAddressValue.setTypeface(fontSemiBold);
        text_copy.setTypeface(fontSemiBold);

        textView_hotWalletAddressValue.setText(BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId());
        text_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Bitcoin Address", textView_hotWalletAddressValue.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(UpdateHotWalletBeneficiaryActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    sendRequestToServer();
                }
            }
        });


        //init data
        hotWalletBeneficiary.setText(BTMApplication.getInstance().getBTMUserObj().getHotWalletBenificiaryKey());
    }


    /**
     * sending hot waller beneficiary update request to server
     */
    private void sendRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_UPDATE_HOT_WALLET_BENEFICIARY;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("merchantId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("hotWalletBenificiaryKey", hotWalletBeneficiary.getText().toString());

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }


    /**
     * validating edit text fields
     * @return
     */
    boolean validateFields() {
        if (hotWalletBeneficiary.getText().toString().isEmpty()) {
            AlertMessage.showError(hotWalletBeneficiary, "Please enter merchant margin profit margin threshold");
            hotWalletBeneficiary.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * this function will be called when the server throws an
     * error when failed to connect to server
     * @param error
     */    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        AlertMessage.showError(hotWalletBeneficiary, Constants.ERROR_CHECK_INTERNET);

    }

    /**
     * this function will be called when the update beneficiary request is
     * successfully executed by the server
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
                    AlertMessage.show(hotWalletBeneficiary, "Success");

                    if (!serverMessageResponse.getData().isEmpty()) {
                        Gson gsonForUser = new Gson();
                        BTMUser btmUser = gsonForUser.fromJson(serverMessageResponse.getData(), BTMUser.class);
                        BTMApplication.getInstance().setBTMUserObj(btmUser);
                        if (isFromRegistration) {
                            startActivity(new Intent(UpdateHotWalletBeneficiaryActivity.this, ProfitWalletOptionActivity.class)
                                    .putExtra(EXTRA_FROM_REGISTRATION, true));
                        } else {
                            finish();
                        }
                    }
                } else {
                    AlertMessage.showError(hotWalletBeneficiary, serverMessageResponse.getMessage());
                }//end oe else
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null

    }

}
