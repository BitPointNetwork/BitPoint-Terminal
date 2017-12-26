package com.ideofuzion.btm.main.settings;

import android.app.Activity;
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
 * this activity is used to create bitpoint profit wallet address
 * in the application
 */

public class BitpointProfitWalletActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener, Constants.ResultCode {
    public static final String EXTRA_IS_FROM_BITPOINT_PROFIT = "profit_wallet";
    TextView text_minMaxBalance_header;
    EditText bitpointProfitWalletKrakenBenificiaryKey;
    EditText bitpointProfitWalletAddress;
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
            setContentView(R.layout.activity_bitpoint_profit_wallet);

            initResources();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {

        }
    }

    /**
     * getting data from intent, init font objects and getting reference to ui elements
     * setting fonts to ui resources and applying  click listeners to it.
     *
     */
    public void initResources() {

        //getting data from intent
        isFromRegistration = getIntent().getBooleanExtra(EXTRA_FROM_REGISTRATION, false);

        //init dialog helper object
        dialogHelper = new DialogHelper(this);


        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(this).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(this).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(this).getTypefaceBold();

        //init ui resources
        text_minMaxBalance_header = (TextView) findViewById(R.id.text_minMaxBalance_header);
        bitpointProfitWalletKrakenBenificiaryKey = (EditText) findViewById(R.id.bitpointProfitWalletKrakenBenificiaryKey);
        bitpointProfitWalletAddress = (EditText) findViewById(R.id.bitpointProfitWalletAddress);
        button_minMaxBalance_submit = (Button) findViewById(R.id.button_minMaxBalance_submit);

        //applying fonts to ui resources
        text_minMaxBalance_header.setTypeface(fontBold);
        bitpointProfitWalletAddress.setTypeface(fontSemiBold);
        bitpointProfitWalletKrakenBenificiaryKey.setTypeface(fontSemiBold);
        button_minMaxBalance_submit.setTypeface(fontBold);
        cancel = (Button) findViewById(R.id.cancel);


        //adding click listners to ui resouces
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        if (isFromRegistration) {
            bitpointProfitWalletAddress.setText(BTMApplication.getInstance().getBTMUserObj().getBitpointProfitWalletAddress());
            cancel.setVisibility(View.GONE);
        } else {
            bitpointProfitWalletAddress.setText(BTMApplication.getInstance().getBTMUserObj().getBitpointProfitWalletAddress());
            bitpointProfitWalletKrakenBenificiaryKey.setText(BTMApplication.getInstance().getBTMUserObj().getBitpointProfitWalletKrakenBenificiaryKey());
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

        bitpointProfitWalletAddress.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (view.getRight() - bitpointProfitWalletAddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        startActivityForResult(new Intent(BitpointProfitWalletActivity.this, ScanQRActivity.class)
                                .putExtra(EXTRA_IS_FROM_BITPOINT_PROFIT, true),110);
                        return true;
                    }
                }
                return false;
            }
        });

        //init data
        bitpointProfitWalletKrakenBenificiaryKey.setText(BTMApplication.getInstance().getBTMUserObj().getBitpointProfitWalletKrakenBenificiaryKey());
        bitpointProfitWalletAddress.setText(BTMApplication.getInstance().getBTMUserObj().getBitpointProfitWalletAddress());

    }//end of onCreate



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
            bitpointProfitWalletAddress.setText(qr);
        }
    }//end of onActivityResult

    /**
     * sending create bitpoint profit wallet request to server
     */
    private void sendRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_CREATE_BITPOINT_PROFIT_WALLET;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("merchantId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("bitpointProfitWalletKrakenBenificiaryKey", bitpointProfitWalletKrakenBenificiaryKey.getText().toString());
        updateTaglineParams.put("bitpointProfitWalletAddress", bitpointProfitWalletAddress.getText().toString());

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }


    /**
     * validating form fields
     * @return
     */
    boolean validateFields() {
        if (bitpointProfitWalletAddress.getText().toString().isEmpty()) {
            AlertMessage.showError(bitpointProfitWalletAddress, "Please enter bitpoint Profit Wallet Address");
            bitpointProfitWalletAddress.requestFocus();
            return false;
        }


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
        AlertMessage.showError(bitpointProfitWalletAddress, Constants.ERROR_CHECK_INTERNET);

    }

    /**
     *this fucntion will be called when the server successfully executes
     * bitpoint wallet creation request
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
                    AlertMessage.show(bitpointProfitWalletAddress, "Success");

                    if (!serverMessageResponse.getData().isEmpty()) {
                        Gson gsonForUser = new Gson();
                        BTMUser btmUser = gsonForUser.fromJson(serverMessageResponse.getData(), BTMUser.class);
                        BTMApplication.getInstance().setBTMUserObj(btmUser);
                        if (isFromRegistration) {
                            startActivity(new Intent(BitpointProfitWalletActivity.this, SetupProfitMarginActivity.class)
                                    .putExtra(EXTRA_FROM_REGISTRATION, true));
                        } else {
                            finish();
                        }

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
