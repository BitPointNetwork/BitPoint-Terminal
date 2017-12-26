package com.ideofuzion.btm.main.scanqr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.gms.vision.CameraSource;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.ideofuzion.btm.BTMApplication;
import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.enteramount.EnterAmountActivity;
import com.ideofuzion.btm.main.sell.SellBitcoinActivity;
import com.ideofuzion.btm.model.QRModel;
import com.ideofuzion.btm.permission.PermissionActivity;
import com.ideofuzion.btm.utils.AlertMessage;
import com.ideofuzion.btm.utils.Constants;
import com.ideofuzion.btm.utils.DialogHelper;
import com.ideofuzion.btm.utils.Fonts;
import com.ideofuzion.btm.utils.MyUtils;
import com.ideofuzion.btm.utils.PermissionHandler;

import me.dm7.barcodescanner.core.ViewFinderView;

import static com.ideofuzion.btm.main.buy.BuyActivity.EXTRA_IS_BUYING;
import static com.ideofuzion.btm.main.settings.BitpointProfitWalletActivity.EXTRA_IS_FROM_BITPOINT_PROFIT;


/**
 * Created by ideofuzion on 6/5/2017.
 *
 * this activity us use to scan the qr code from other application
 * and fetch the data from the qr code
 */

public class ScanQRActivity extends Activity implements QRCodeReaderView.OnQRCodeReadListener {
    private static final int CAMERA_PERMISSION_ID = 1;
    //my resources
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private Typeface fontBold;
    private boolean flashStatus;
    TextView text_scanQRFragment_dollarRate, text_scanQRFragment_title,
            text_scanQRFragment_description, text_scanQRFragment_scanning;
    Button button_scanQRFragment_cancel;
    LinearLayout linearLayout_scanQRFragment_scannerContainer;
    DialogHelper dialogHelper;
    PermissionHandler permissionHandler;
    boolean isBuying = false;
    boolean isFromBitcoinScan;
    QRModel qrModel = null;
    private QRCodeReaderView qrCodeReaderView;

    /**
     * this function will be called
     * when activity starts and
     * setting up init setup required to start activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_scan_qr);
            initResources(savedInstanceState);

            iniTypeface();

            addListener();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
        }
    }

    /**
     * adding listener to ui elements
     */
    private void addListener() {
        button_scanQRFragment_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    /**
     * applying fonts to ui elements
     */
    private void iniTypeface() {
        text_scanQRFragment_dollarRate.setTypeface(fontSemiBold);
        text_scanQRFragment_title.setTypeface(fontBold);
        text_scanQRFragment_description.setTypeface(fontRegular);
        text_scanQRFragment_scanning.setTypeface(fontRegular);
        button_scanQRFragment_cancel.setTypeface(fontRegular);
    }

    /**
     * this function will be called each time
     * the there is config changes and we are saving some data
     * so that the data retains even if the activity restarts
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_IS_BUYING, isBuying);
        outState.putBoolean(EXTRA_IS_FROM_BITPOINT_PROFIT,isFromBitcoinScan);
    }

    /**
     * getting data from intents
     * getting reference to xml ui elements
     * setting up init data
     *
     * @param savedInstanceState
     */
    private void initResources(Bundle savedInstanceState) {
        //gettings data from intents if data exists
        if (savedInstanceState != null) {
            isBuying = savedInstanceState.getBoolean(EXTRA_IS_BUYING, false);
            isFromBitcoinScan =savedInstanceState.getBoolean(EXTRA_IS_FROM_BITPOINT_PROFIT,false);
        } else {
            isBuying = getIntent().getBooleanExtra(EXTRA_IS_BUYING, false);
            isFromBitcoinScan =getIntent().getBooleanExtra(EXTRA_IS_FROM_BITPOINT_PROFIT,false);
        }

        //init permission handler obj
        permissionHandler = new PermissionHandler(this);

        //init dialopg helper obj
        dialogHelper = new DialogHelper(this);

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(getApplicationContext()).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(getApplicationContext()).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(getApplicationContext()).getTypefaceBold();

        //init ui elements and setting up init data
        text_scanQRFragment_dollarRate = (TextView) findViewById(R.id.text_scanQRFragment_dollarRate);
        text_scanQRFragment_title = (TextView) findViewById(R.id.text_scanQRFragment_title);
        text_scanQRFragment_title.setText(Html.fromHtml("<i>Scan QR Code</i>"));
        text_scanQRFragment_description = (TextView) findViewById(R.id.text_scanQRFragment_description);
        text_scanQRFragment_scanning = (TextView) findViewById(R.id.text_scanQRFragment_scanning);
        text_scanQRFragment_scanning.setText(Html.fromHtml("<i>Scanning...</i>"));

        button_scanQRFragment_cancel = (Button) findViewById(R.id.button_scanQRFragment_cancel);
        linearLayout_scanQRFragment_scannerContainer = (LinearLayout) findViewById(R.id.linearLayout_scanQRFragment_scannerContainer);
        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setAutofocusInterval(1000);
        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);


        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();


        //init data
        text_scanQRFragment_description.setText("Open your wallet and place your QR code in front of the camera");
        if (isBuying)
            text_scanQRFragment_dollarRate.setText("1 BTC = " + MyUtils.getDecimalFormattedAmount(BTMApplication.getInstance().getSellingRate()) + Constants.CURRENCY);
        else
            text_scanQRFragment_dollarRate.setText("1 BTC = " + MyUtils.getDecimalFormattedAmount(BTMApplication.getInstance().getBuyingRate()) + Constants.CURRENCY);


    }

    /**
     * this fucntion will be called each time the activity starts
     * performing camera permission check and starting camera if permission exists
     * else prompting user to start camera
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        if (permissionHandler.isPermissionAvailable(Manifest.permission.CAMERA))
            qrCodeReaderView.startCamera();
        else {
            permissionHandler.requestPermission(Manifest.permission.CAMERA);
        }
    }


    /**
     * this function will be called
     * when user deny or grant the prompted permission
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_ID && grantResults.length > 0 && grantResults[0] > -1) {
            qrCodeReaderView.startCamera();
        } else {
            Intent intent = new Intent(this, PermissionActivity.class);
            intent.putExtra(PermissionActivity.PARAM_PERMISSION_CAMERA, true);
            startActivity(intent);
        }
    }


    /**
     * this function will be called when the
     * activity go to pause state and stoping camera in that state
     */
    @Override
    public void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }


    /**
     * this function will be called when user scan qr and
     * the result of that scan will be returned in this function
     * @param data
     * @param points
     */
 @Override
 public void onQRCodeRead(String data, PointF[] points) {

     if (data != null) {


         try {
             if(data.contains("bitcoin:")){
                String[]bitcoinStrArr= data.split(":");
                 data=bitcoinStrArr[1];
             }
             qrModel = new QRModel();
             qrModel.setBitcoin(data);
             if(qrModel.getBitcoin()== null)
                 throw new NullPointerException();
             BTMApplication.getInstance().setQrModel(qrModel);
             if(isFromBitcoinScan){
                 Intent intent = new Intent();
                 intent.putExtra("address",qrModel.getBitcoin());
                 setResult(Activity.RESULT_OK,intent);
                    finish();
             }else {
                 if (isBuying)
                     startActivity(new Intent(ScanQRActivity.this, EnterAmountActivity.class));
                 else {
                     startActivity(new Intent(ScanQRActivity.this, SellBitcoinActivity.class)
                             .putExtra("address", qrModel.getBitcoin()));
                 }
             }

         } catch (Exception e) {
             qrModel = null;
             AlertMessage.showError(text_scanQRFragment_description, "No appropriate data found");
             //mScannerView.resumeCameraPreview(this);
         }
     }
 }
}
