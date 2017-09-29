package com.ideofuzion.btm.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ideofuzion.btm.R;
import com.ideofuzion.btm.utils.MyUtils;
import com.ideofuzion.btm.utils.PermissionHandler;
import com.ideofuzion.btm.utils.Fonts;

//call this activity when permision popup blocked by user
public class PermissionActivity extends AppCompatActivity {


    private static final int PERMISSIONS_REQUEST = 786;
    public static final String PARAM_PERMISSION_STORAGE = "storage";
    public static final String PARAM_PERMISSION_CAMERA = "camera";
    public static final String PARAM_PERMISSION_PHONE_STATS = "phone_stats";
    public static final String PARAM_PERMISSION_CONTACTS = "contacts";
    public static final String PARAM_PERMISSION_ACCOUNTS = "accounts";
    ImageView img_permission_icon;
    AppCompatTextView txt_permission_tittle;
    AppCompatTextView txt_permission_description;
    Button btn_permission_enable;
    private String permissionQuery;
    PermissionHandler permissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        permissionHandler = new PermissionHandler(this);
        initResources();
        setTypeFace();
        addViewListner();
        initPermissionScreen();
    }//end of onCreate

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults != null && grantResults.length > 0 && grantResults[0] == 0) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }//end of if for permission granted
            else if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                int resultCode = MyUtils.getPermissionStatus(PermissionActivity.this, permissionQuery);
                if (resultCode == MyUtils.BLOCKED_OR_NEVER_ASKED) {
                    //Here redirect to setting for getting permision
                    MyUtils.redirectToAppSettingScreen(PermissionActivity.this);
                }
            }//end of iff for permision denied

        }//end of if fro permision request
    }//end of permsiion callback

    @Override
    protected void onResume() {
        super.onResume();
        //when user go to setting and return back so we should finish activity as user granted permisison
        int resultCode = MyUtils.getPermissionStatus(PermissionActivity.this, permissionQuery);
        if (resultCode == MyUtils.GRANTED) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    //hanler all listners of this screen
    private void addViewListner() {
        btn_permission_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionHandler.requestPermission(permissionQuery, PERMISSIONS_REQUEST);
            }//end of on Click
        });
    }

    //Apply typeface from assets
    private void setTypeFace() {
        txt_permission_tittle.setTypeface(Fonts.getInstance(this).getTypefaceSemiBold());
        txt_permission_description.setTypeface(Fonts.getInstance(this).getTypefaceSemiBold());
        btn_permission_enable.setTypeface(Fonts.getInstance(this).getTypefaceBold());
    }

    //here all xml resources bind
    private void initResources() {
        img_permission_icon = (ImageView) findViewById(R.id.img_permission_icon);
        txt_permission_tittle = (AppCompatTextView) findViewById(R.id.txt_permission_tittle);
        txt_permission_description = (AppCompatTextView) findViewById(R.id.txt_permission_description);
        btn_permission_enable = (Button) findViewById(R.id.btn_permission_enable);
    }

    //Init here all config
    private void initPermissionScreen() {
      /*  if (getIntent().getBooleanExtra(PARAM_PERMISSION_CONTACTS, false)) {
            setContactPermission();
        } else*/
        if (getIntent().getBooleanExtra(PARAM_PERMISSION_CAMERA, false)) {
            setCameraPermission();
        } /*else if (getIntent().getBooleanExtra(PARAM_PERMISSION_STORAGE, false)) {
            setStoragePermision();
        } else if (getIntent().getBooleanExtra(PARAM_PERMISSION_PHONE_STATS, false)) {
            setPhonePermission();
        } else if (getIntent().getBooleanExtra(PARAM_PERMISSION_ACCOUNTS, false)) {
            setAccountPermision();
        } else {
            setStoragePermision();
        }*/

    }//end of initPermiion Screen

  /*  private void setStoragePermision() {
        txt_permission_tittle.setText(getString(R.string.storage_title));
        txt_permission_description.setText(getString(R.string.storage_description));
        img_permission_icon.setImageResource(R.drawable.img_permission_storage);
        permissionQuery = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    }

    private void setAccountPermision() {
        txt_permission_tittle.setText(getString(R.string.contact_permisison_tiltle));
        txt_permission_description.setText(getString(R.string.account_description));
        img_permission_icon.setImageResource(R.drawable.img_permission_contacts);
        permissionQuery = Manifest.permission.WRITE_CONTACTS;
    }

    private void setPhonePermission() {
        txt_permission_tittle.setText(getString(R.string.phone_state_tiltle));
        txt_permission_description.setText(getString(R.string.phone_state_description));
        img_permission_icon.setImageResource(R.drawable.img_permission_contacts);
        permissionQuery = Manifest.permission.READ_PHONE_STATE;
    }

    private void setContactPermission() {
        txt_permission_tittle.setText(getString(R.string.contact_permisison_tiltle));
        txt_permission_description.setText(getString(R.string.contact_permisison_description));
        img_permission_icon.setImageResource(R.drawable.img_permission_contacts);
        permissionQuery = Manifest.permission.WRITE_CONTACTS;
    }*/

    private void setCameraPermission() {
        txt_permission_tittle.setText("Camera Access");
        txt_permission_description.setText("Please allow BTM to access camera");
        permissionQuery = Manifest.permission.CAMERA;
        // img_permission_icon.setImageResource(R.drawable.img_permission_camera);

    }

    //we are using this function to not work as user remain on this screen
    @Override
    public void onBackPressed() {
        //   super.onBackPressed();
    }
}//end of class
