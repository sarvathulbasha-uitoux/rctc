package com.google.firebase.example.fireeats.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by UITOUX5 on 06/03/18.
 */

public class EditTextOswaldRegular extends AppCompatEditText {
    public EditTextOswaldRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-Regular.ttf"));
    }
}
