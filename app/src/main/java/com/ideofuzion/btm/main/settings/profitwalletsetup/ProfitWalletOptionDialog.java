package com.ideofuzion.btm.main.settings.profitwalletsetup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ideofuzion.btm.R;
import com.ideofuzion.btm.utils.Fonts;

/**
 * Created by khali on 9/24/2017.
 */

public class ProfitWalletOptionDialog {

    Dialog dialog;
    private Typeface fontRegular;
    private Typeface fontSemiBold;
    private Typeface fontBold;

    public void show(final Context context) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profit_wallet_options);
        TextView text_dialog_header = (TextView) dialog.findViewById(R.id.text_dialog_header);
        Button button_dialog_newAccount = (Button) dialog.findViewById(R.id.button_dialog_newAccount);
        Button button_dialog_existingAccount = (Button) dialog.findViewById(R.id.button_dialog_existingAccount);

        //initializing TypeFaces objects
        fontRegular = Fonts.getInstance(context).getTypefaceRegular();
        fontSemiBold = Fonts.getInstance(context).getTypefaceSemiBold();
        fontBold = Fonts.getInstance(context).getTypefaceBold();

        text_dialog_header.setTypeface(fontBold);
        button_dialog_newAccount.setTypeface(fontBold);
        button_dialog_existingAccount.setTypeface(fontBold);

        dialog.show();
        button_dialog_existingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExistingProfitWalletDialog().show(context);
                dialog.dismiss();
            }
        });
        button_dialog_newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewProfitWalletDialog().show(context);
                dialog.dismiss();
            }
        });

    }
}
