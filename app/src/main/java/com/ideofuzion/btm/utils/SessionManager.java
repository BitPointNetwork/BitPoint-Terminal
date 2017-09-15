package com.ideofuzion.btm.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.main.login.LoginActivity;
import com.ideofuzion.btm.main.buy.BuyActivity;
import com.ideofuzion.btm.model.BTMUser;
import com.ideofuzion.btm.model.ServerMessage;
import com.ideofuzion.btm.network.VolleyRequestHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khali on 6/7/2017.
 */

public class SessionManager implements Constants.ResultCode, Response.Listener<JSONObject>,
        Response.ErrorListener {

    public static SessionManager sessionManager;
    static Context mContext;
    static DialogHelper dialogHelper;

    public static SessionManager getInstance(Context context) {
        mContext = context;
        dialogHelper = new DialogHelper(context);

        if (sessionManager == null) {
            return new SessionManager();
        }
        return sessionManager;
    }

    public String getPass() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("session", mContext.MODE_PRIVATE);
        return sharedPreferences.getString("password", "");
    }

    public void createSession(String email, String password) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("session", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.commit();
    }

    public boolean hasSession() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("session", mContext.MODE_PRIVATE);
        if (sharedPreferences.contains("email")) {
            dialogHelper.showProgressDialog();
            sendSignInRequestToServer(sharedPreferences.getString("email", ""), sharedPreferences.getString("password", ""));
            return true;
        } else {
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
            ((BuyActivity) mContext).finish();
        }
        return false;
    }

    private void sendSignInRequestToServer(String username, String password) {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_USER_LOGIN;

        String deviceModel = android.os.Build.MODEL;
        //String deviceOS = android.os.Build.
        String deviceOSVersion = android.os.Build.VERSION.RELEASE; // e.g. myVersion := "1.6"
        String deviceOSName = "Android";
        Map<String, String> signUpParams = new HashMap<>();
        signUpParams.put("userName", username);
        signUpParams.put("userMobileUniqueId", deviceModel);
        signUpParams.put("userMobileOSVersion", deviceOSVersion);
        signUpParams.put("userMobileOSName", deviceOSName);
        signUpParams.put("userPassword", password);
        VolleyRequestHelper.sendPostRequestWithParam(url, signUpParams, SessionManager.this);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        mContext.startActivity(new Intent(mContext, LoginActivity.class));

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
                    redirectUserAfterSuccessSignIn(serverMessageResponse.getData());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null
    }

    public void redirectUserAfterSuccessSignIn(String data) {
        if (!data.isEmpty()) {
            Gson gsonForUser = new Gson();
            BTMUser btmUser = gsonForUser.fromJson(data, BTMUser.class);
            BTMApplication.getInstance().setBTMUserObj(btmUser);
        }
    }//end of redirect UserAfter SuccessSigniIn

}
