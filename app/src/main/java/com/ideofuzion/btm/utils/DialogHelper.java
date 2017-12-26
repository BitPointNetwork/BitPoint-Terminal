package com.ideofuzion.btm.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by ideofuzion on 4/8/2017.
 *
 * this class is the loader class all the loading in the application
 * is achieved from this class
 */



public class DialogHelper {

    ProgressDialog progressDialog;
    Context context;

    public DialogHelper(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
    }

    public void showProgressDialog() {
        try {
            if (progressDialog != null)
                progressDialog.show();
        }catch (Exception e){

        }
    }

    public void hideProgressDialog() {
        try {
            if (progressDialog != null)
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
        }catch (Exception e)
        {}
    }
}
