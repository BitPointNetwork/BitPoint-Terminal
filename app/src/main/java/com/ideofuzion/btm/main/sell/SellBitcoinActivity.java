package com.ideofuzion.btm.main.sell;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static com.ideofuzion.btm.main.settings.PinCodeActivity.EXTRA_FROM_REGISTRATION;
import static com.ideofuzion.btm.main.transfercomplete.TransferCompleteActivity.EXTRA_AMOUNT_TO_BE_GIVEN;

/**
 * Created by khali on 9/23/2017.
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
        text_sell_header = (TextView) findViewById(R.id.text_sell_header);
        linearLayout_sell_buttonContainer = (LinearLayout) findViewById(R.id.linearLayout_sell_buttonContainer);
        button_sell_startListening = (Button) findViewById(R.id.button_sell_startListening);
        linearLayout_sell_loadingContainer = (LinearLayout) findViewById(R.id.linearLayout_sell_loadingContainer);
        text_sell_loadingText = (TextView) findViewById(R.id.text_sell_loadingText);

        QRGEncoder qrgEncoder = new QRGEncoder("bitcoin:" + BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId(), null, QRGContents.Type.TEXT, 350);
        try {
            // Getting QR-Code as Bitmap
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            imageView_sell_qrCode.setImageBitmap(bitmap);
            // Setting Bitmap to ImageView
        } catch (Exception e) {
            e.printStackTrace();
        }

        text_sell_header.setTypeface(fontBold);
        button_sell_startListening.setTypeface(fontSemiBold);
        text_sell_loadingText.setTypeface(fontSemiBold);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        button_sell_startListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestToServer();
            }
        });

    }

    private void sendRequestToServer() {
        String url = Constants.BASE_SERVER_URL + Constants.ROUTE_SAVE_SENDER_ADDRESS;

        Map<String, String> updateTaglineParams = new HashMap<>();
        updateTaglineParams.put("receiverAddress", BTMApplication.getInstance().getBTMUserObj().getUserBitcoinId());
        updateTaglineParams.put("fcmId", FirebaseInstanceId.getInstance().getToken());
        updateTaglineParams.put("senderAddress", senderAddress);

        VolleyRequestHelper.sendPostRequestWithParam(url, updateTaglineParams, this);
        dialogHelper.showProgressDialog();

    }


    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialogHelper != null) {
            dialogHelper.hideProgressDialog();
        }
        linearLayout_sell_buttonContainer.setVisibility(View.VISIBLE);
        linearLayout_sell_loadingContainer.setVisibility(View.GONE);
        AlertMessage.showError(text_sell_header, Constants.ERROR_CHECK_INTERNET);

    }

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
                    AlertMessage.show(text_sell_header, "Success");
                    linearLayout_sell_buttonContainer.setVisibility(View.GONE);
                    linearLayout_sell_loadingContainer.setVisibility(View.VISIBLE);
                } else {
                    linearLayout_sell_buttonContainer.setVisibility(View.VISIBLE);
                    linearLayout_sell_loadingContainer.setVisibility(View.GONE);
                    AlertMessage.showError(text_sell_header, serverMessageResponse.getMessage());
                }//end oe else
            } catch (Exception e) {
                e.printStackTrace();
            }

        }//end of if for response not null

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverSuccessConfirmation, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager
                .getInstance(this).unregisterReceiver(broadcastReceiverSuccessConfirmation);
    }

    BroadcastReceiver broadcastReceiverSuccessConfirmation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                startActivity(new Intent(SellBitcoinActivity.this, TransferCompleteActivity.class)
                        .putExtra("senderAddress", intent.getStringExtra("senderAddress"))
                        .putExtra(EXTRA_AMOUNT_TO_BE_GIVEN, intent.getStringExtra("amount")));
                finish();
            }
        }
    };

}
