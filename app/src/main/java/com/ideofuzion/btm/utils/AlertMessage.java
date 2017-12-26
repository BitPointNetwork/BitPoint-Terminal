package com.ideofuzion.btm.utils;

import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.ideofuzion.btm.R;


/**
 * Created by ideofuzion on 4/10/2017.
 *
 * this class has static functions that are used
 * to show error or other information messages from all around the application
 */

public class AlertMessage {
    /**
     * this function shows error
     * @param view this is view towhome the error is associated
     * @param message this is message to display
     */
    public static void showError(View view, String message)
    {
        if(message != null) {
            Typeface fontSemiBold = Fonts.getInstance(view.getContext()).typefaceSemiBold;
            final ForegroundColorSpan whiteSpan = new ForegroundColorSpan(ContextCompat.getColor(view.getContext(), R.color.error_color));
            SpannableStringBuilder snackbarText = new SpannableStringBuilder(message);
            if (snackbarText != null) {
                snackbarText.setSpan(whiteSpan, 0, snackbarText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                snackbarText.setSpan(new Fonts.MultiCustomTypeFaceSpan("", fontSemiBold),
                        0, snackbarText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                Snackbar.make(view, snackbarText, Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    /**
     * this function shows information message
     * @param view this is view towhome the error is associated
     * @param message this is message to display
     */
    public static void show(View view, String message)
    {
        Typeface fontSemiBold = Fonts.getInstance(view.getContext()).typefaceSemiBold;
        final ForegroundColorSpan whiteSpan = new ForegroundColorSpan(ContextCompat.getColor(view.getContext(), R.color.text_color));
        SpannableStringBuilder snackbarText = new SpannableStringBuilder(message);
        if(snackbarText != null) {
            snackbarText.setSpan(whiteSpan, 0, snackbarText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            snackbarText.setSpan(new Fonts.MultiCustomTypeFaceSpan("", fontSemiBold),
                    0, snackbarText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            Snackbar.make(view, snackbarText, Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
