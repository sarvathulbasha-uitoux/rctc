package com.google.firebase.example.fireeats.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by UITOUX5 on 06/03/18.
 */

public class TextViewOswaldRegular extends AppCompatTextView {
    public TextViewOswaldRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-Regular.ttf"));
    }
}
