package com.dss.swmusic.custom.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dss.swmusic.R;

public class RecordView extends FrameLayout {

    private final String TAG = "RecordView";

    private ImageView imageView;

    private ValueAnimator recordAnimator = ValueAnimator.ofFloat(0,360);

    public RecordView(@NonNull Context context) {
        this(context,null);
    }

    public RecordView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_record,this,false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(view,params);
        imageView = view.findViewById(R.id.cover);
        // 设置动画
        recordAnimator.setDuration(15000);
        recordAnimator.setRepeatMode(ValueAnimator.RESTART);
        recordAnimator.setRepeatCount(ValueAnimator.INFINITE);
        recordAnimator.setInterpolator(new LinearInterpolator());
    }

    public void setImage(String url){
        Log.e(TAG, "setImage: "+url );
        Glide.with(imageView)
                .load(url)
                .into(imageView);
    }

    public void startAnimation(){
        if(recordAnimator.isPaused()){
            recordAnimator.resume();
        }else{
            recordAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    setRotation(value);
                }
            });
            recordAnimator.start();
        }
    }

    public void stopAnimation(){
        recordAnimator.pause();
    }

    public void resetAnimation(){
        setRotation(0);
        recordAnimator.end();
    }

}
