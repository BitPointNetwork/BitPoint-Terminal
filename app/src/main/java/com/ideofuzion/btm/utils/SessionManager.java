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

public class SessionManager  {

    public static SessionManager sessionManager;
    static Context mContext;

    public static SessionManager getInstance(Context context) {
        mContext = context;

        if (sessionManager == null) {
            return new SessionManager();
        }
        return sessionManager;
    }

    public String getPass() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("session", mContext.MODE_PRIVATE);
        return sharedPreferences.getString("password", "");
    }
    public String getEmail() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("session", mContext.MODE_PRIVATE);
        return sharedPreferences.getString("email", "");
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
            return true;
        }
        return false;
    }



}
