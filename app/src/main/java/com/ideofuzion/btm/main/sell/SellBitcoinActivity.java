package com.ideofuzion.btm.main.sell;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.transfercomplete.TransferCompleteActivity;
import com.ideofuzion.btm.model.ServerMessage;
import com.ideofuzion.btm.network.VolleyRequestHelper;
import com.ideofuzion.btm.utils.AlertMessage;
import com.ideofuzion.btm.utils.Constants;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.MyUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.ideofuzion.btm.main.settings.PinCodeActivity.EXTRA_FROM_REGISTRATION;
import static com.ideofuzion.btm.main.transfercomplete.TransferCompleteActivity.EXTRA_AMOUNT_TO_BE_GIVEN;

/**
 * Created by ideofuzion on 9/23/2017.
 *
 * this activity is used to sell bitcoin user will be navigated to this
 * activity if he want to sell bitcoins the rate the application is sending bitcoin is
 * displayed on the left top of this screen as well
 */

public class SellBitcoinActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener, Constants.ResultCode {
    TextView text_sell_header;
    LinearLayout linearLayout_sell_buttonContainer;
    Button cancel;
    Button button_sell_startListening;
    LinearLayout linearLayout_sell_loadingContainer;
    TextView text_sell_loadingText;
    ImageView imageView_sell_qrCode;
    private Typeface fontBold;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private DialogHelper dialogHelper;
    private boolean isFromRegistration = false;
    String senderAddress;
    public static final String SUCCESS_CONFRIMATION_ACTION = "confirmationAction";
    IntentFilter intentFilter;
    TextView txt_sell_merchantwalletaddr;

    /**
     * this function will be called each time
     * the activity starts and the init setting are setup here
     * to run the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_sell);
            initResources();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
        }
    }

    /**
     * getting data from intent
     * getting reference to ui resources from xml
     * init dialog helper object
     * init font object
     * init initial data
     */
    public void initResources() {
        intentFilter = new IntentFilter(SUCCESS_CONFRIMATION_ACTION);
        senderAddress = getIntent().getStringExtra("address");

        isFromRegistration = getIntent().getBooleanExtra(EXTRA_FROM_REGISTRATION, false);

        dialogHelper = new DialogHelper(this);
        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(this).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(this).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(this).getTypefaceBold();
        imageView_sell_qrCode = (ImageView) findViewById(R.id.imageView_sell_qrCode);
        text_sell_header = (TextView) findViewById(R.id.text_sell_header);
        linearLayout_sell_buttonContainer = (LinearLayout) findViewById(R.id.linearLayout_sell_buttonContainer);
        button_sell_startListening = (Button) findViewById(R.id.button_sell_startListening);
        linearLayout_sell_loadingContainer = (LinearLayout) findViewById(R.id.linearLayout_sell_loadingContainer);
        text_sell_loadingText = (TextView) findViewById(R.id.text_sell_loadingText);
        txt_sell_merchantwalletaddr = (TextView) findViewById(R.id.txt_sell_merchantwalletaddr);

//        QRGEncoder qrgEncoder = new QRGEncoder("bitcoin:" + BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId(), null, QRGContents.Type.TEXT, 350);
        QRGEncoder qrgEncoder = new QRGEncoder(BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId(), null, QRGContents.Type.TEXT, 350);
        try {
            // Getting QR-Code as Bitmap
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            imageView_sell_qrCode.setImageBitmap(bitmap);
            // Setting Bitmap to ImageView
        } catch (Exception e) {
            e.printStackTrace();
        }
        txt_sell_merchantwalletaddr.setText("Merchant Address : " + BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId());

        text_sell_header.setTypeface(fontBold);
        button_sell_startListening.setTypeface(fontSemiBold);
        text_sell_loadingText.setTypeface(fontSemiBold);
        txt_sell_merchantwalletaddr.setTypeface(fontSemiBold);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        sendRequestToServer();


    }

    /**
     * sending save sender request to server
     */

    private void sendRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_SAVE_SENDER_ADDRESS;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("receiverAddress", BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId());
        updateTaglineParams.put("fcmId", FirebaseInstanceId.getInstance().getToken());

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }


    /**
     this function will be called when the server throws an
     * error when failed to connect to server
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        linearLayout_sell_buttonContainer.setVisibility(View.VISIBLE);
        linearLayout_sell_loadingContainer.setVisibility(View.GONE);
        AlertMessage.showError(text_sell_header, Constants.ERROR_CHECK_INTERNET);

    }

    /**
     * this function will be called each time the
     * server successfully execute over request
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
                serverMessageResponse.setCode(response.getInt("code"));
                serverMessageResponse.setMessage(response.getString("message"));
                if (serverMessageResponse.getCode() == CODE_SUCCESS) {
                    AlertMessage.show(text_sell_header, serverMessageResponse.getMessage());
                    linearLayout_sell_buttonContainer.setVisibility(View.GONE);
                    linearLayout_sell_loadingContainer.setVisibility(View.VISIBLE);
                } else {
                    linearLayout_sell_buttonContainer.setVisibility(View.GONE);
                    linearLayout_sell_loadingContainer.setVisibility(View.GONE);
                    AlertMessage.showError(text_sell_header, serverMessageResponse.getMessage());
                }//end oe else
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null

    }

    /**
     * this function will be called when
     * activity starts and registering a broadcast receiver
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverSuccessConfirmation, intentFilter);
    }

    /**
     * this function will be called
     * when the activity goes to pause state
     * and unregistering the broadcast receiver
     */
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager
                .getInstance(this).unregisterReceiver(broadcastReceiverSuccessConfirmation);
    }

    SweetAlertDialog pDialog;
    /**
     * this is broadcast receiver and this will
     * be called when ever a broadcast is send
     */
    BroadcastReceiver broadcastReceiverSuccessConfirmation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
               String type = intent.getStringExtra("type");
                if(type.equalsIgnoreCase("transactionUnConfirmed")){
                    String txId = intent.getStringExtra("txId");
                    if(txId!=null) {
//                        pDialog = new SweetAlertDialog(SellBitcoinActivity.this, SweetAlertDialog.PROGRESS_TYPE);
//                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//                        pDialog.setTitleText("Uncofirmed Transaction Recieved");
//                        pDialog.setContentText("Transaction Hash : "+txId);
//                        pDialog.setCancelable(true);
//                        pDialog.setCanceledOnTouchOutside(true);
//                        pDialog.show();
                        text_sell_loadingText.setText("Tracking Transaction hash : "+txId);
                        linearLayout_sell_loadingContainer.setVisibility(View.VISIBLE);
                    }

                }else{
                    if(pDialog!=null){
                        pDialog.dismissWithAnimation();
                    }
                    startActivity(new Intent(SellBitcoinActivity.this, TransferCompleteActivity.class)
                            .putExtra("senderAddress", intent.getStringExtra("senderAddress"))
                            .putExtra(EXTRA_AMOUNT_TO_BE_GIVEN, intent.getStringExtra("amount")));
                    finish();
                }
            }//end of intent not null
        }
    };

}
