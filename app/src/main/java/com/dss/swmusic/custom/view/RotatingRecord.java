package com.dss.swmusic.custom.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.dss.swmusic.R;

/**
 * 旋转的唱片
 * 自定义view，组合控件
 */
public class RotatingRecord extends FrameLayout {

    private RotateAnimation pinAnimation;
    private ValueAnimator recordAnimator;
    private ImageView pin;

    public RotatingRecord(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_view_rotating_record,this);
        pin = view.findViewById(R.id.pin);
        ImageView record = view.findViewById(R.id.record);

        //设置针压动画
        pinAnimation = new RotateAnimation(0,-30,0,0);
        pinAnimation.setDuration(2000);
        pinAnimation.setFillAfter(true);
        pin.startAnimation(pinAnimation);

        //设置唱片的动画
        recordAnimator = ValueAnimator.ofFloat(0,360);
        recordAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                record.setRotation(value);
            }
        });
        recordAnimator.setDuration(6000);
        recordAnimator.setRepeatMode(ValueAnimator.RESTART);
        recordAnimator.setRepeatCount(ValueAnimator.INFINITE);
        recordAnimator.setInterpolator(new LinearInterpolator());

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAnimation(boolean flag){
        if(flag){
            pin.startAnimation(pinAnimation);
            if(!recordAnimator.isStarted()){
                recordAnimator.start();
            }else{
                recordAnimator.resume();
            }
        }else{
            pin.clearAnimation();
            recordAnimator.pause();
        }
    }

}
