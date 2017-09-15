package com.ideofuzion.btm.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Zohaib Khaliq on 4/8/2017.
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
