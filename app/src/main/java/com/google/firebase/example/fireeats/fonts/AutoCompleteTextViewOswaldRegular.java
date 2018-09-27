package com.google.firebase.example.fireeats.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

/**
 * Created by uitouxserver on 26/03/18.
 */

public class AutoCompleteTextViewOswaldRegular extends AppCompatAutoCompleteTextView {
    public AutoCompleteTextViewOswaldRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-Regular.ttf"));
    }
}
