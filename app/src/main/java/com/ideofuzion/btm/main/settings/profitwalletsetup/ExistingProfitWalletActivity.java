package com.ideofuzion.btm.main.settings.profitwalletsetup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
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
import com.ideofuzion.btm.main.scanqr.ScanQRActivity;
import com.ideofuzion.btm.main.settings.BitpointProfitWalletActivity;
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

import static com.ideofuzion.btm.main.settings.BitpointProfitWalletActivity.EXTRA_IS_FROM_BITPOINT_PROFIT;
import static com.ideofuzion.btm.main.settings.PinCodeActivity.EXTRA_FROM_REGISTRATION;

/**
 * Created by ideofuzion on 9/23/2017.
 *
 * this activity is used to add existing profit wallet
 * in the application and the request is send to server
 */

public class ExistingProfitWalletActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener, Constants.ResultCode {
    TextView text_minMaxBalance_header;
    EditText profitWalletKrakenBenificiaryKey;
    EditText profitWalletAddress;
    Button button_minMaxBalance_submit;
    private Typeface fontBold;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private DialogHelper dialogHelper;
    private boolean isFromRegistration = false;
    private Button cancel;
    View editText_bottom_benificiary_line;

    /**
     * this function will be called each time the activity starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_exisitng_profit_wallet);
            initResources();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception e) {
        }

    }

    /**
     *
     * getting data from intent, initiating ui resources and dialog helper object
     */
    public void initResources() {

        isFromRegistration = getIntent().getBooleanExtra(EXTRA_FROM_REGISTRATION, false);


        dialogHelper = new DialogHelper(this);
        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(this).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(this).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(this).getTypefaceBold();

        text_minMaxBalance_header = (TextView) findViewById(R.id.text_minMaxBalance_header);
        profitWalletKrakenBenificiaryKey = (EditText) findViewById(R.id.edit_minMaxBalance_minBalance);
        profitWalletAddress = (EditText) findViewById(R.id.edit_minMaxBalance_maxBalance);
        button_minMaxBalance_submit = (Button) findViewById(R.id.button_minMaxBalance_submit);
        editText_bottom_benificiary_line = findViewById(R.id.editText_bottom_benificiary_line);
        text_minMaxBalance_header.setTypeface(fontBold);
        profitWalletAddress.setTypeface(fontSemiBold);
        profitWalletKrakenBenificiaryKey.setTypeface(fontSemiBold);
        button_minMaxBalance_submit.setTypeface(fontBold);
        cancel = (Button) findViewById(R.id.cancel);
        profitWalletAddress.setText(BTMApplication.getInstance().getBTMUserObj().getProfitWalletAddress());
        profitWalletKrakenBenificiaryKey.setText(BTMApplication.getInstance().getBTMUserObj().getProfitWalletKrakenBenificiaryKey());
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
        button_minMaxBalance_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    sendRequestToServer();
                }
            }
        });

        profitWalletAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_scan_qr, 0);
        profitWalletAddress.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (view.getRight() - profitWalletAddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        startActivityForResult(new Intent(ExistingProfitWalletActivity.this, ScanQRActivity.class)
                                .putExtra(EXTRA_IS_FROM_BITPOINT_PROFIT, true),110);
                        return true;
                    }
                }
                return false;
            }
        });

        //init data
        profitWalletKrakenBenificiaryKey.setText(BTMApplication.getInstance().getBTMUserObj().getProfitWalletKrakenBenificiaryKey());

    }

    /**
     * this function will return the result from scanner activity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==110&&resultCode ==RESULT_OK){
            String qr = data.getStringExtra("address");
            profitWalletAddress.setText(qr);
        }
    }//end of onActivityResult


    /**
     * send create profit wallet to server
     */
    private void sendRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_CREATE_PROFIT_WALLET;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("merchantId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("createNewProfitWallet", "0");
        updateTaglineParams.put("profitWalletKrakenBenificiaryKey", profitWalletKrakenBenificiaryKey.getText().toString().trim());
        updateTaglineParams.put("profitWalletAddress", profitWalletAddress.getText().toString().trim());

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }

    /**
     * validating fields return true if validated else
     * will return false
     * @return
     */
    boolean validateFields() {
        //No Need
       /* if (profitWalletAddress.getText().toString().isEmpty()) {
            AlertMessage.showError(profitWalletAddress, "Please enter Profit Wallet Address");
            profitWalletAddress.requestFocus();
            return false;
        }*/
        return true;
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
        AlertMessage.showError(profitWalletAddress, Constants.ERROR_CHECK_INTERNET);

    }

    /**
     * thi function will be called when server successfully executes
     * profit wallet create request
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
                    AlertMessage.show(profitWalletAddress, "Success");

                    if (!serverMessageResponse.getData().isEmpty()) {
                        Gson gsonForUser = new Gson();
                        BTMUser btmUser = gsonForUser.fromJson(serverMessageResponse.getData(), BTMUser.class);
                        BTMApplication.getInstance().setBTMUserObj(btmUser);
                        if (isFromRegistration) {
                            startActivity(new Intent(ExistingProfitWalletActivity.this,
                                    BuyActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        } else {
                            finish();
                        }
                    }
                } else {
                    AlertMessage.showError(profitWalletAddress, serverMessageResponse.getMessage());
                }//end oe else
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null

    }

}
