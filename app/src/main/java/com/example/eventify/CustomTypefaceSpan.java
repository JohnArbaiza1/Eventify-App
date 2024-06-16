package com.example.eventify;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

public class CustomTypefaceSpan extends MetricAffectingSpan {

    private final Typeface typeface;

    public CustomTypefaceSpan(String family, Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public void updateMeasureState(TextPaint ds) {
        applyCustomTypeFace(ds, typeface);
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        applyCustomTypeFace(paint, typeface);
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
