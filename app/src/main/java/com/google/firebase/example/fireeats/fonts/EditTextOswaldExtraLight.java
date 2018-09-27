package com.google.firebase.example.fireeats.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by uitouxserver on 12/03/18.
 */

public class EditTextOswaldExtraLight extends AppCompatEditText {
    public EditTextOswaldExtraLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-ExtraLight.ttf"));
    }
}