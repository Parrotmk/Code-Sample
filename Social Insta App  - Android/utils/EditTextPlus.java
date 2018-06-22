package com.whaddyalove.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.whaddyalove.common.CustomFontHelper;


public class EditTextPlus extends android.support.v7.widget.AppCompatEditText {

    public EditTextPlus(Context context) {
        super(context);
    }

    public EditTextPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setEditTextCustomFont(this, context, attrs);
    }

    public EditTextPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setEditTextCustomFont(this, context, attrs);
    }
}