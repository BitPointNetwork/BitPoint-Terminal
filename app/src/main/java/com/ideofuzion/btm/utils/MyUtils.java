package com.ideofuzion.btm.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by khali on 9/23/2017.
 */

public class MyUtils {
    public static boolean isNullOrEmpty(String string) {
        if (string == null || string.length() == 0)
            return true;
        return false;
    }


    volatile public static Context context;

    public static boolean isInternetConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static void setErrorTextColor(TextInputLayout textInputLayout, int color) {
        try {
            Field fErrorView = TextInputLayout.class.getDeclaredField("mErrorView");
            fErrorView.setAccessible(true);
            TextView mErrorView = (TextView) fErrorView.get(textInputLayout);
            Field fCurTextColor = TextView.class.getDeclaredField("mCurTextColor");
            fCurTextColor.setAccessible(true);
            fCurTextColor.set(mErrorView, color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setUnreadNotificationStatus(Context context, boolean status) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences("ethereum", Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putBoolean("status", status);

        editor.apply();
    }

    public static boolean getUnreadNotificationStatus(Context context) {
        SharedPreferences settings;
        settings = context.getSharedPreferences("ethereum", Context.MODE_PRIVATE);
        return settings.getBoolean("status", false);
    }

    /*@SuppressWarnings("MissingPermission")
    public static boolean isEtherAccountExist(Activity context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] allEthAccounts = accountManager.getAccountsByType(context.getString(R.string.ether_account_type));
        if (allEthAccounts != null && allEthAccounts.length > 0) {
            return true;
        }//end of if
        return false;
    }*/

    public static void hideKeyboardFromActivity(Activity context) {
        InputMethodManager im = (InputMethodManager) context
                .getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(context.getWindow().getDecorView()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public static void openNetworkSetting() {
        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }


    public static void rotateAnimation(View img_over_drum, float angle) {
        // TODO Auto-generated method stub

        RotateAnimation animation = new RotateAnimation(angle, angle + 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        angle = (angle + 0.5f) % 360;

        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1000);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);

        img_over_drum.startAnimation(animation);

    }

    public static String getDeviceUniqueID(Activity mActivity)

    {

        // Toast.makeText(context, "ANDROID ID", Toast.LENGTH_LONG).initResources();
        String UniqueID = Settings.Secure.getString(mActivity.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        return UniqueID;
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRANTED, DENIED, BLOCKED_OR_NEVER_ASKED})
    public @interface PermissionStatus {
    }

    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;

    @PermissionStatus
    public static int getPermissionStatus(Activity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return BLOCKED_OR_NEVER_ASKED;
            }
            return DENIED;
        }
        return GRANTED;
    }

    /**
     * for redirecting to setting screen
     *
     * @param activity referecnce where function call
     * @return true if redirect successfully
     */
    public static boolean redirectToAppSettingScreen(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
            return true;
        }//end of if
        return false;
    }//end of function


/*
    public static void showFullScreenImageTransition(Activity mActivity, String urlForProfile, View mTransiotalImage, boolean... isFromSdCarDImage) {
        final Intent intent = new Intent(mActivity, ZoomImageActiviity.class);
        if (isFromSdCarDImage != null && isFromSdCarDImage.length > 0) {
            intent.putExtra("isFromSdCard", true);
        }
        intent.putExtra("url", urlForProfile);
        String transitionName = mActivity.getString(R.string.transition_profile);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                        mTransiotalImage,   // Starting view
                        transitionName    // The String
                );
        //Start the Intent
        ActivityCompat.startActivity(mActivity, intent, options.toBundle());

    }//end of function
*/

    public static HashMap<String, String> getDeviceDataForRequest(Activity activity) {
        HashMap<String, String> loginParams = new HashMap<>();
        String deviceModel = android.os.Build.MODEL;
        String baseOS = Build.VERSION.RELEASE;
        String deviceUniqueId = getDeviceUniqueID(activity);
        String token = FirebaseInstanceId.getInstance().getToken();
        loginParams.put("userMobileUniqueId", deviceUniqueId);
        loginParams.put("userMobileOSName", "Android");
        loginParams.put("userMobileOSVersion", baseOS);
        loginParams.put("userDeviceName", deviceModel);
        if (token != null)
            loginParams.put("userGCM", token);
        return loginParams;
    }

    public static String getPasswordStregth(String password) {
        final String PASSWORD_PATTERN_VERY_STRONG =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        final String PASSWORD_PATTERN_STRONG =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
        final String PASSWORD_PATTERN_WEAK =
                "((?=.*[a-z])(?=.*[A-Z]).{6,20})";

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN_VERY_STRONG);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return PaswordStrength.VeryStrong.toString();
        }
        pattern = Pattern.compile(PASSWORD_PATTERN_STRONG);
        matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return PaswordStrength.Strong.toString();
        }
        pattern = Pattern.compile(PASSWORD_PATTERN_WEAK);
        matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return PaswordStrength.Weak.toString();
        }
        return PaswordStrength.Weak.toString();
    }//end of function

    enum PaswordStrength {
        Weak("Weak"), Strong("Strong"), VeryStrong("Very Strong");
        private final String stringValue;

        PaswordStrength(final String s) {
            stringValue = s;
        }

        public String toString() {
            return stringValue;
        }
        // further methods, attributes, etc.
    }

    public static void showSnackBar(Activity context, String message) {
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) context
                .findViewById(android.R.id.content)).getChildAt(0);
        Snackbar snack = Snackbar.make(viewGroup, message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

   /* public static String getFormattedMobileNumber(String cellPhone) {

        if (cellPhone != null && !TextUtils.isEmpty(cellPhone)) {
            cellPhone = cellPhone.replace(" ", "");
            if (cellPhone.contains("+") || cellPhone.substring(0, 1).equalsIgnoreCase("00")) {
            } else {
                if (String.valueOf(cellPhone.charAt(0)).equalsIgnoreCase("0")) {
                    cellPhone = cellPhone.substring(1, cellPhone.length());
                }
                return "+" + EthereumApplication.getInstance().countryCode + cellPhone;
            }
        }
        return cellPhone;
    }*/

    public static void pullDbFile(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + context.getPackageName() + "/databases/ethereum.db";
                String backupDBPath = "ethereum.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideKeyboardOnTocuhAnyWhereElse(final Activity activity, View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText) || !(view instanceof TextInputEditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    if (v.hasFocus()) {
                        v.clearFocus();
                    }
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboardOnTocuhAnyWhereElse(activity, innerView);
            }
        }
    }
}
