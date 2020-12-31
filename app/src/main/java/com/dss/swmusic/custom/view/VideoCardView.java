package com.dss.swmusic.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.material.card.MaterialCardView;

/**
 * 高 / 宽 = 0.5425
 */
public class VideoCardView extends MaterialCardView {

    public VideoCardView(Context context) {
        super(context);
    }

    public VideoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
//        Log.e("tag","width = "+width);
        int height = (int) (width*0.5425);
//        Log.e("tag","height="+height);
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }
}
