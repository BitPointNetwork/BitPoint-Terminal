package com.ideofuzion.btm.main.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
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
import com.ideofuzion.btm.main.register.RegisterActivity;
import com.ideofuzion.btm.model.BTMUser;
import com.ideofuzion.btm.model.ServerMessage;
import com.ideofuzion.btm.network.VolleyRequestHelper;
import com.ideofuzion.btm.utils.AlertMessage;
import com.ideofuzion.btm.utils.Constants;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.Internet;
import com.ideofuzion.btm.utils.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khali on 6/19/2017.
 */

public class LoginActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener, Constants.ResultCode {
    EditText edit_login_email, edit_login_password;
    Button button_login_login;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private Typeface fontBold;
    TextView text_loginActivity_signUp;
    DialogHelper dialogHelper;
    boolean s = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login);

            initResources();

            initTypeface();

            addListener();

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            if (new SessionManager().getInstance(this).hasSession()) {
                s = true;
                dialogHelper.showProgressDialog();
                sendSignInRequestToServer(new SessionManager().getInstance(this).getEmail(), new SessionManager().getInstance(this).getPass());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addListener() {
        button_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        edit_login_password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    signIn();
                }
                return false;
            }
        });
    }

    private void signIn() {
        if (validateFields()) {
            if (Internet.isConnected(LoginActivity.this)) {
                dialogHelper.showProgressDialog();
                sendSignInRequestToServer(edit_login_email.getText().toString(), edit_login_password.getText().toString());
            }
        }
    }

    private void initTypeface() {
        edit_login_email.setTypeface(fontSemiBold);
        edit_login_password.setTypeface(fontSemiBold);
        button_login_login.setTypeface(fontBold);
    }

    private void initResources() {

        dialogHelper = new DialogHelper(this);

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(getApplicationContext()).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();

        edit_login_email = (EditText) findViewById(R.id.edit_login_email);
        edit_login_password = (EditText) findViewById(R.id.edit_login_password);
        button_login_login = (Button) findViewById(R.id.button_login_login);
        text_loginActivity_signUp = (TextView) findViewById(R.id.text_loginActivity_signUp);

        makeTextSignInSignUpClickable(text_loginActivity_signUp);
    }

    private void makeTextSignInSignUpClickable(TextView text_signIn_signUp) {
        SpannableString ss = new SpannableString("Not a Registered member? SIGN UP");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //MyUtils.sendEventToFirebase("SignInActivity_SignUpClicked");
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_theme_color)),
                24, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new Fonts.MultiCustomTypeFaceSpan("", fontBold),
                24, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new Fonts.MultiCustomTypeFaceSpan("", fontRegular),
                0, 24, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.textView_forgotPassword_color)),
                0, 24, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        text_signIn_signUp.append(ss);
        text_signIn_signUp.setMovementMethod(LinkMovementMethod.getInstance());
        text_signIn_signUp.setHighlightColor(Color.TRANSPARENT);
    }

    private void sendSignInRequestToServer(String username, String password) {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_USER_LOGIN;

        String deviceModel = android.os.Build.MODEL;
        //String deviceOS = android.os.Build.
        String deviceOSVersion = android.os.Build.VERSION.RELEASE; // e.g. myVersion := "1.6"
        String deviceOSName = "Android";
        Map<String, String> signInParams = new HashMap<>();
        signInParams.put("userName", username);
        signInParams.put("userMobileUniqueId", deviceModel);
        signInParams.put("userMobileOSVersion", deviceOSVersion);
        signInParams.put("userMobileOSName", deviceOSName);
        signInParams.put("userPassword", password);
/*
        signInParams.put("userRole",2+"");
*/
        VolleyRequestHelper.sendPostRequestWithParam(url, signInParams, this);
    }

    private boolean validateFields() {

        if (edit_login_email.getText().toString().equals("")) {
            AlertMessage.showError(edit_login_email, Constants.ERROR_EMPTY_EMAIL);
            edit_login_email.requestFocus();
            return false;
        }
        if (edit_login_password.getText().toString().equals("")) {
            AlertMessage.showError(edit_login_password, Constants.ERROR_EMPTY_PASSWORD);
            edit_login_password.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        AlertMessage.showError(edit_login_email, Constants.ERROR_CHECK_INTERNET);
        s = false;

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
                    SessionManager.getInstance(getApplicationContext()).createSession(edit_login_email.getText().toString(), edit_login_password.getText().toString());
                    redirectUserAfterSuccessSignIn(serverMessageResponse.getData());
                } else {
                    if (!s)
                        AlertMessage.showError(edit_login_email, serverMessageResponse.getMessage());
                }//end oe else
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null
        s = false;
    }

    private void redirectUserAfterSuccessSignIn(String data) {
        if (!data.isEmpty()) {
            Gson gsonForUser = new Gson();
            BTMUser btmUser = gsonForUser.fromJson(data, BTMUser.class);
            BTMApplication.getInstance().setBTMUserObj(btmUser);
            startActivity(new Intent(LoginActivity.this, BuyActivity.class));
            finish();
        }
    }
}
