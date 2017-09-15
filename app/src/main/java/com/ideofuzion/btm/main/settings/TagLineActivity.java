package com.ideofuzion.btm.main.settings;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.KeyEvent;
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
import com.ideofuzion.btm.utils.Internet;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khali on 6/19/2017.
 */

public class TagLineActivity extends Activity implements Constants.ResultCode, Response.Listener<JSONObject>,
        Response.ErrorListener {

    TextView text_tagline_title, text_settings_header, text_tagline_dollarRate;
    EditText edit_tagline_tagline;
    Button button_tagline_cancel, button_tagline_set;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private Typeface fontBold;
    DialogHelper dialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_tag_line);

            initResources();

            initTypefaces();

            addListener();
        }catch (Exception e)
        {}
    }

    private void addListener() {
        button_tagline_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTagline();
            }
        });
        button_tagline_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit_tagline_tagline.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    updateTagline();
                }
                return false;
            }
        });
    }

    private void updateTagline() {
        if (!edit_tagline_tagline.getText().toString().isEmpty()) {
            if (Internet.isConnected(TagLineActivity.this)) {
                dialogHelper.showProgressDialog();
                sendTaglineUpdateRequestToServer();
            } else {
                AlertMessage.showError(edit_tagline_tagline, Constants.ERROR_NO_INTERNET);
            }
        } else {
            AlertMessage.showError(edit_tagline_tagline, "Please ente tagline.");
        }
    }

    private void initTypefaces() {
        text_settings_header.setTypeface(fontSemiBold);
        text_tagline_title.setTypeface(fontBold);
        text_tagline_dollarRate.setTypeface(fontSemiBold);
        edit_tagline_tagline.setTypeface(fontSemiBold);
        button_tagline_cancel.setTypeface(fontBold);
        button_tagline_set.setTypeface(fontBold);
    }

    private void initResources() {
        dialogHelper = new DialogHelper(this);

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(getApplicationContext()).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();

        text_settings_header = (TextView) findViewById(R.id.text_settings_header);
        text_tagline_title = (TextView) findViewById(R.id.text_tagline_title);
        text_tagline_dollarRate = (TextView) findViewById(R.id.text_tagline_dollarRate);
        edit_tagline_tagline = (EditText) findViewById(R.id.edit_tagline_tagline);
        button_tagline_cancel = (Button) findViewById(R.id.button_tagline_cancel);
        button_tagline_set = (Button) findViewById(R.id.button_tagline_set);

        text_tagline_title.setText(Html.fromHtml("<i>Enter Tagline</i>"));
        edit_tagline_tagline.setHint(Html.fromHtml("<i>Maximum 180 Characters</i>"));

        text_tagline_dollarRate.setText("1 BTC = " + BTMApplication.getInstance().getBTMUserObj().getBitcoinDollarRate() + " USD");

    }

    private void sendTaglineUpdateRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_UPDATE_TAGLINE;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("userId", BTMApplication.getInstance().getBTMUserObj().getUserId());
        updateTaglineParams.put("tagLine", edit_tagline_tagline.getText().toString());
        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        AlertMessage.showError(edit_tagline_tagline, Constants.ERROR_CHECK_INTERNET);

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
                        redirectUserAfterSuccessTaglineUpdate(serverMessageResponse.getData());
                    } else {
                        AlertMessage.showError(edit_tagline_tagline, serverMessageResponse.getMessage());
                    }//end oe else
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//end of if for server mesage not null
        }
    }

    private void redirectUserAfterSuccessTaglineUpdate(String data) {
        if (!data.isEmpty()) {
            AlertMessage.show(edit_tagline_tagline, "Successfully Updated");

            Gson gsonForUser = new Gson();
            BTMUser btmUser = gsonForUser.fromJson(data, BTMUser.class);
            btmUser.setBitcoinDollarRate(BTMApplication.getInstance().getBTMUserObj().getBitcoinDollarRate());
            BTMApplication.getInstance().setBTMUserObj(btmUser);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 500);

        }
    }
}
