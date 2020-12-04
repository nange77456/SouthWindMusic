package com.dss.swmusic.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainTabItem extends androidx.appcompat.widget.AppCompatTextView {

    public MainTabItem(@NonNull Context context) {
        super(context);
        init();
    }

    public MainTabItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainTabItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
//        setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
    }



}
