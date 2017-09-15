package com.ideofuzion.btm.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class Fonts {
    static Fonts instance;
    static Context applicationContext;


    Typeface typefaceRegular, typefaceSemiBold, typefaceBold;//,typefaceItalic,typefaceSemiBoldItalic;

    private Fonts() {
        typefaceRegular = getOpenSansRegularTypeFace();
        typefaceSemiBold = getOpenSansSemiBoldTypeFace();
        typefaceBold = getOpenSansBoldTypeFace();
        //typefaceItalic = getOpenSansItalicTypeFace();
        //typefaceSemiBoldItalic = getOpenSansSemiBoldItalicTypeFace();
    }

    public static Fonts getInstance(Context context) {
        applicationContext = context;
        if (instance == null) {
            instance = new Fonts();
        }
        return instance;
    }

    public Typeface getTypefaceRegular() {
        return typefaceRegular;
    }

    public Typeface getTypefaceSemiBold() {
        return typefaceSemiBold;
    }

    public Typeface getTypefaceBold() {
        return typefaceBold;
    }

    private Typeface getOpenSansBoldTypeFace() {
        return Typeface.createFromAsset(applicationContext.getAssets(),
                "Fonts/OpenSans-Bold.ttf");
    }

    private Typeface getOpenSansRegularTypeFace() {
        return Typeface.createFromAsset(applicationContext.getAssets(),
                "Fonts/OpenSans-Regular.ttf");
    }

    private Typeface getOpenSansSemiBoldTypeFace() {
        return Typeface.createFromAsset(applicationContext.getAssets(),
                "Fonts/OpenSans-Semibold.ttf");
    }

    /*public Typeface getOpenSansItalicTypeFace() {
        return Typeface.createFromAsset(applicationContext.getAssets(),
                "Fonts/OPENSANS-ITALIC.ttf");
    }
    public Typeface getOpenSansSemiBoldItalicTypeFace() {
        return Typeface.createFromAsset(applicationContext.getAssets(),
                "Fonts/OPENSANS-SEMIBOLDITALIC.ttf");
    }*/

    @SuppressLint("ParcelCreator")
    public static class MultiCustomTypeFaceSpan extends TypefaceSpan {

        private final Typeface newType;

        public MultiCustomTypeFaceSpan(String family, Typeface type) {
            super(family);
            newType = type;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeFace(paint, newType);
        }

        private static void applyCustomTypeFace(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTypeface(tf);
        }
    }

}
