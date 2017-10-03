package com.ideofuzion.btm.main.settings.profitwalletsetup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ideofuzion.btm.R;
import com.ideofuzion.btm.main.settings.PinCodeActivity;
import com.ideofuzion.btm.utils.Fonts;

import static com.ideofuzion.btm.main.settings.PinCodeActivity.EXTRA_FROM_REGISTRATION;

/**
 * Created by khali on 9/24/2017.
 */

public class ProfitWalletOptionActivity extends Activity {

    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private Typeface fontBold;
    private boolean isFromRegistration = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_profit_wallet_options);
            initResources();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }catch (Exception e)
        {}
    }

    public void initResources() {

        isFromRegistration = getIntent().getBooleanExtra(EXTRA_FROM_REGISTRATION, false);


        TextView text_dialog_header = (TextView) findViewById(R.id.text_dialog_header);
        Button button_dialog_newAccount = (Button) findViewById(R.id.button_dialog_newAccount);
        Button button_dialog_existingAccount = (Button) findViewById(R.id.button_dialog_existingAccount);

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(this).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(this).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(this).getTypefaceBold();

        text_dialog_header.setTypeface(fontBold);
        button_dialog_newAccount.setTypeface(fontBold);
        button_dialog_existingAccount.setTypeface(fontBold);

        button_dialog_existingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromRegistration) {
                    startActivity(new Intent(ProfitWalletOptionActivity.this, ExistingProfitWalletActivity.class)
                            .putExtra(EXTRA_FROM_REGISTRATION, true));
                } else {
                    startActivity(new Intent(ProfitWalletOptionActivity.this, ExistingProfitWalletActivity.class));
                    finish();
                }

            }
        });
        button_dialog_newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromRegistration) {
                    startActivity(new Intent(ProfitWalletOptionActivity.this, NewProfitWalletActivity.class)
                            .putExtra(EXTRA_FROM_REGISTRATION, true));
                } else {
                    startActivity(new Intent(ProfitWalletOptionActivity.this, NewProfitWalletActivity.class));
                    finish();
                }
            }
        });

    }
}
