package com.whaddyalove.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.whaddyalove.common.CustomFontHelper;


/**
 * Created by user on 8/4/2015.
 */
public class ButtonPlus extends android.support.v7.widget.AppCompatButton {

    public ButtonPlus(Context context) {
        super(context);
    }

    public ButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    public ButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }
}
