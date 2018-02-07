package com.ideofuzion.btm.main.settings;

import android.app.Activity;
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
 * this activity is used to  create kraken setup in th application
 */

public class KrakenSetupActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener, Constants.ResultCode {
    TextView text_krakenSetup_header;
    EditText edit_krakenSetup_krakenApiKey;
    EditText edit_krakenSetup_krakenApiSecret;
    Button button_krakenSetup_submit;
    private Typeface fontBold;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private DialogHelper dialogHelper;
    private boolean isFromRegistration = false;
    private Button cancel;
    public static String EXTRA_SKIP_KRAKEN = "skip_kraken";

    /**
     * this function will be called each time the activity starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_kraken_setup);
            initResources();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
        }
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

        text_krakenSetup_header = (TextView) findViewById(R.id.text_minMaxBalance_header);
        edit_krakenSetup_krakenApiKey = (EditText) findViewById(R.id.edit_minMaxBalance_minBalance);
        edit_krakenSetup_krakenApiSecret = (EditText) findViewById(R.id.edit_minMaxBalance_maxBalance);
        button_krakenSetup_submit = (Button) findViewById(R.id.button_minMaxBalance_submit);

        //applying fonts to ui resources
        text_krakenSetup_header.setTypeface(fontBold);
        edit_krakenSetup_krakenApiSecret.setTypeface(fontSemiBold);
        edit_krakenSetup_krakenApiKey.setTypeface(fontSemiBold);
        button_krakenSetup_submit.setTypeface(fontBold);
        cancel = (Button) findViewById(R.id.cancel);

        /**
         * adding click listeners to ui resources
         */
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cancel.getText().toString().equalsIgnoreCase("Cancel")){
                finish();
                } else{
                    sendRequestToSkipKraken();
                }
            }

        });
        cancel.setTypeface(fontBold);
        if (isFromRegistration) {
            cancel.setText("Skip");
            cancel.setVisibility(View.VISIBLE);
        } else {
            cancel.setText("Cancel");
            cancel.setVisibility(View.VISIBLE);
        }
        button_krakenSetup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    sendRequestToServer();
                }
            }
        });


        //init data
        edit_krakenSetup_krakenApiKey.setText(BTMApplication.getInstance().getBTMUserObj().getKrakenAPIKey());
        edit_krakenSetup_krakenApiSecret.setText(BTMApplication.getInstance().getBTMUserObj().getKrakenAPISecret());

    }

    /**
     * sending kraken key update request to server
     */
    private void sendRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_UPDATE_KRAKEN_KEYS;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("merchantId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("krakenAPIKey", edit_krakenSetup_krakenApiKey.getText().toString());
        updateTaglineParams.put("krakenAPISecret", edit_krakenSetup_krakenApiSecret.getText().toString());
        if(isFromRegistration){
        updateTaglineParams.put("isToggleChange",String.valueOf(1));
        }
        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }

    /**
     * sending kraken skip request to server
     */
    private void sendRequestToSkipKraken(){
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_UPDATE_USE_KRAKEN;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("merchantId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("useKraken", 0+"");

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, new KrakenDismiss());
        dialogHelper.showProgressDialog();
    }

    /**
     * validating form edit fields
     * @return
     */
    boolean validateFields() {
        if (edit_krakenSetup_krakenApiKey.getText().toString().isEmpty()) {
            AlertMessage.showError(edit_krakenSetup_krakenApiSecret, "Please Enter kraken API key");
            edit_krakenSetup_krakenApiKey.requestFocus();
            return false;
        }
        if (edit_krakenSetup_krakenApiSecret.getText().toString().isEmpty()) {
            AlertMessage.showError(edit_krakenSetup_krakenApiSecret, "Please Enter kraken API secret key");
            edit_krakenSetup_krakenApiSecret.requestFocus();
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
        AlertMessage.showError(edit_krakenSetup_krakenApiSecret, Constants.ERROR_CHECK_INTERNET);

    }

    /**
     * this function will be called when server
     * successfully executes the request of setting up or skipping kraken
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
                    AlertMessage.show(edit_krakenSetup_krakenApiSecret, "Success");

                    if (!serverMessageResponse.getData().isEmpty()) {
                        Gson gsonForUser = new Gson();
                        BTMUser btmUser = gsonForUser.fromJson(serverMessageResponse.getData(), BTMUser.class);
                        BTMApplication.getInstance().setBTMUserObj(btmUser);
                        if (isFromRegistration) {
                            startActivity(new Intent(KrakenSetupActivity.this, BitpointProfitWalletActivity.class)
                                    .putExtra(EXTRA_FROM_REGISTRATION, true));
                        } else {
                            finish();
                        }

                    }
                } else {
                    AlertMessage.showError(edit_krakenSetup_krakenApiSecret, serverMessageResponse.getMessage());
                }//end oe else
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null

    }
    class KrakenDismiss implements  Response.Listener<JSONObject>, Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            if (dialogHelper != null) {
                dialogHelper.hideProgressDialog();
            }
            Toast.makeText(KrakenSetupActivity.this,"Please Try Agian",Toast.LENGTH_LONG).show();
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
                        AlertMessage.show(edit_krakenSetup_krakenApiSecret, "Success");

                        if (!serverMessageResponse.getData().isEmpty()) {
                            Gson gsonForUser = new Gson();
                            BTMUser btmUser = gsonForUser.fromJson(serverMessageResponse.getData(), BTMUser.class);
                            BTMApplication.getInstance().setBTMUserObj(btmUser);
                            if (isFromRegistration) {
                                startActivity(new Intent(KrakenSetupActivity.this, BitpointProfitWalletActivity.class)
                                        .putExtra(EXTRA_FROM_REGISTRATION, true));
                            } else {
                                finish();
                            }

                        }
                    } else {
                        AlertMessage.showError(edit_krakenSetup_krakenApiSecret, serverMessageResponse.getMessage());
                    }//end oe else
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }//en

        }
    }

}
