package com.ideofuzion.btm.main.register;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
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
import com.ideofuzion.btm.main.settings.PinCodeActivity;
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
 * Created by khali on 6/1/2017.
 */

public class RegisterActivity extends Activity implements Constants.ResultCode, Response.Listener<JSONObject>,
        Response.ErrorListener {
    EditText edit_registerActivity_name, edit_registerActivity_email,
            edit_registerActivity_phoneNumber, edit_registerActivity_password,
            edit_registerActivity_confirmPassword;
    Button button_registerActivity_register;
    TextView text_registerActivity_signIn;
    Typeface fontRegular;
    Typeface fontSemiBold;
    Typeface fontBold;
    DialogHelper dialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_register);
            initResources();

            initTypefaces();

            addListener();

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
        }
    }

    private void addListener() {
        edit_registerActivity_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    edit_registerActivity_name.setText(result);
                    edit_registerActivity_name.setSelection(result.length());
                    // alert the user
                }
            }
        });
        button_registerActivity_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        edit_registerActivity_password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    edit_registerActivity_confirmPassword.requestFocus();
                }
                return false;
            }
        });
        edit_registerActivity_confirmPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    signUp();
                }
                return false;
            }
        });
    }

    private void signUp() {
        startActivity(new Intent(RegisterActivity.this, PinCodeActivity.class)
        .putExtra(PinCodeActivity.EXTRA_FROM_REGISTRATION,true));
        /*if (Internet.isConnected(RegisterActivity.this)) {
            if (validateFields()) {
                dialogHelper.showProgressDialog();
                sendSignUpRequestToServer();
            }
        } else {
            AlertMessage.showError(edit_registerActivity_email, Constants.ERROR_NO_INTERNET);
        }*/
    }

    private void initTypefaces() {
        edit_registerActivity_name.setTypeface(fontRegular);
        edit_registerActivity_email.setTypeface(fontRegular);
        edit_registerActivity_phoneNumber.setTypeface(fontRegular);
        edit_registerActivity_confirmPassword.setTypeface(fontRegular);
        edit_registerActivity_password.setTypeface(fontRegular);
        button_registerActivity_register.setTypeface(fontBold);
    }


    private void initResources() {
        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(getApplicationContext()).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();


        dialogHelper = new DialogHelper(this);
        edit_registerActivity_name = (EditText) findViewById(R.id.edit_registerActivity_name);
        edit_registerActivity_email = (EditText) findViewById(R.id.edit_registerActivity_email);
        edit_registerActivity_phoneNumber = (EditText) findViewById(R.id.edit_registerActivity_phoneNumber);
        edit_registerActivity_password = (EditText) findViewById(R.id.edit_registerActivity_password);
        edit_registerActivity_confirmPassword = (EditText) findViewById(R.id.edit_registerActivity_confirmPassword);
        button_registerActivity_register = (Button) findViewById(R.id.button_registerActivity_register);
        text_registerActivity_signIn = (TextView) findViewById(R.id.text_registerActivity_signUp);
        makeTextClickable();

    }

    private void makeTextClickable() {


        //Making sign in text clickable
        SpannableString spannableStringSignInText = new SpannableString("Already Registered? SIGN IN");
        ClickableSpan clickableSpanSignInText = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //MyUtils.sendEventToFirebase("SignUpActivity_SignInClicked");
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableStringSignInText.setSpan(clickableSpanSignInText, 0, spannableStringSignInText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringSignInText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_theme_color)),
                19, spannableStringSignInText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringSignInText.setSpan(new Fonts.MultiCustomTypeFaceSpan("", fontBold),
                19, spannableStringSignInText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringSignInText.setSpan(new Fonts.MultiCustomTypeFaceSpan("", fontRegular),
                0, 19, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringSignInText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.textView_forgotPassword_color)),
                0, 19, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        text_registerActivity_signIn.append(spannableStringSignInText);
        text_registerActivity_signIn.setMovementMethod(LinkMovementMethod.getInstance());
        text_registerActivity_signIn.setHighlightColor(Color.TRANSPARENT);
    }


    private void sendSignUpRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_USER_SIGNUP;

        Map<String, String> signUpParams = new HashMap<>();
        signUpParams.put("userFullName", edit_registerActivity_name.getText().toString());
        signUpParams.put("userEmail", edit_registerActivity_email.getText().toString());
        signUpParams.put("userName", edit_registerActivity_name.getText().toString());
        signUpParams.put("userPassword", edit_registerActivity_password.getText().toString());
        signUpParams.put("userContactNumber", edit_registerActivity_phoneNumber.getText().toString());
        signUpParams.put("userRole", "2");
        VolleyRequestHelper.sendPostRequestWithParam(url, signUpParams, this);

    }

    private boolean validateFields() {
        if (edit_registerActivity_name.getText().toString().equals("")) {
            AlertMessage.showError(edit_registerActivity_name, Constants.ERROR_EMPTY_NAME);
            edit_registerActivity_name.requestFocus();
            return false;
        }
        if (edit_registerActivity_email.getText().toString().equals("")) {
            AlertMessage.showError(edit_registerActivity_email, Constants.ERROR_EMPTY_EMAIL);
            edit_registerActivity_email.requestFocus();
            return false;
        }
        if (!edit_registerActivity_email.getText().toString().contains("@") || !edit_registerActivity_email.getText().toString().contains(".")) {
            AlertMessage.showError(edit_registerActivity_email, Constants.ERROR_EMAIL_FORMAT);
            edit_registerActivity_email.setText("");
            edit_registerActivity_email.requestFocus();
            return false;
        }
        if (edit_registerActivity_phoneNumber.getText().toString().equals("")) {
            AlertMessage.showError(edit_registerActivity_phoneNumber, Constants.ERROR_EMPTY_MOBILE_NUMBER);
            edit_registerActivity_phoneNumber.requestFocus();
            return false;
        }
        if (edit_registerActivity_password.getText().toString().equals("")) {
            AlertMessage.showError(edit_registerActivity_password, Constants.ERROR_EMPTY_PASSWORD);
            edit_registerActivity_password.requestFocus();
            return false;
        }
        if (edit_registerActivity_confirmPassword.getText().toString().equals("")) {
            AlertMessage.showError(edit_registerActivity_confirmPassword, Constants.ERROR_EMPTY_CONFIRMPASSWORD);
            edit_registerActivity_confirmPassword.requestFocus();
            return false;
        }

        if (edit_registerActivity_password.getText().toString().length() < 6) {
            AlertMessage.showError(edit_registerActivity_password, Constants.ERROR_PASSWORD_LENGTH);
            edit_registerActivity_password.requestFocus();
            return false;
        }
        if (!edit_registerActivity_password.getText().toString().equals(edit_registerActivity_confirmPassword.getText().toString())) {
            AlertMessage.showError(edit_registerActivity_password, Constants.ERROR_NO_PASSWORD_MATCH);
            edit_registerActivity_password.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        AlertMessage.showError(edit_registerActivity_email, Constants.ERROR_CHECK_INTERNET);

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
                        redirectUserAfterSuccessSignUp(serverMessageResponse.getData());
                    } else {
                        AlertMessage.showError(edit_registerActivity_email, serverMessageResponse.getMessage());
                    }//end oe else
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//end of if for server mesage not null
        }

    }

    private void redirectUserAfterSuccessSignUp(String data) {
        if (!data.isEmpty()) {
            Gson gsonForUser = new Gson();
            BTMUser btmUser = gsonForUser.fromJson(data, BTMUser.class);
            BTMApplication.getInstance().setBTMUserObj(btmUser);
            SessionManager.getInstance(getApplicationContext()).createSession(edit_registerActivity_email.getText().toString(), edit_registerActivity_password.getText().toString());
            startActivity(new Intent(RegisterActivity.this, BuyActivity.class));
            finish();
        }
    }//end of redirect

}
