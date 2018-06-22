package com.whaddyalove.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.whaddyalove.common.CustomFontHelper;


/**
 * Created by user on 8/4/2015.
 */
public class TextViewPlus extends android.support.v7.widget.AppCompatTextView {

    public TextViewPlus(Context context) {
        super(context);
    }

    public TextViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomTextViewFont(this, context, attrs);
    }

    public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomTextViewFont(this, context, attrs);
    }
}
