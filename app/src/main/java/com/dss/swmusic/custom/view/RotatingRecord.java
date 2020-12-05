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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 旋转的唱片
 * 自定义view，组合控件
 */
public class RotatingRecord extends FrameLayout {
    /**
     * 针压动画——视图动画
     */
    private RotateAnimation pinAnimation1;
    /**
     * 针压动画——视图动画
     */
    private RotateAnimation pinAnimation2;
    /**
     * 唱片动画——值动画
     */
    private ValueAnimator recordAnimator;
    /**
     * 针压图片视图
     */
    private ImageView pin;
    /**
     * 歌曲封面视图
     */
    private CircleImageView cover;
    /**
     * 唱片视图
     */
    private ImageView record;

    public RotatingRecord(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_view_rotating_record,this);
        pin = view.findViewById(R.id.pin);
        //唱片视图
        record = view.findViewById(R.id.record);
        //歌曲封面视图
        cover = view.findViewById(R.id.cover);

        //让针压起始装态是远离唱片的
        pin.setPivotX(dp2px(context,18));
        pin.setPivotY(dp2px(context,16));
        pin.setRotation(-20);

        //设置针压动画1
        pinAnimation1 = new RotateAnimation(0,20,dp2px(context,18),dp2px(context,16));
        pinAnimation1.setDuration(500);
        pinAnimation1.setFillAfter(true);
        //设置针压动画2
        pinAnimation2 = new RotateAnimation(20,0,dp2px(context,18),dp2px(context,16));
        pinAnimation2.setDuration(500);
        pinAnimation2.setFillAfter(true);

        //设置唱片的动画
        recordAnimator = ValueAnimator.ofFloat(0,360);
        recordAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                record.setRotation(value);
                cover.setRotation(value);
            }
        });
        recordAnimator.setDuration(15000);
        recordAnimator.setRepeatMode(ValueAnimator.RESTART);
        recordAnimator.setRepeatCount(ValueAnimator.INFINITE);
        recordAnimator.setInterpolator(new LinearInterpolator());

    }

    /**
     * 启动唱片和针压的动画
     * @param flag
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAnimation(boolean flag){
        if(flag){
            pin.startAnimation(pinAnimation1);
            if(!recordAnimator.isStarted()){
                recordAnimator.start();
            }else{
                recordAnimator.resume();
            }
        }else{
            pin.startAnimation(pinAnimation2);
            recordAnimator.pause();
        }
    }

    /**
     * px转dp静态方法
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dp转px静态方法
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    public void clickRecord(){
        //TODO 单点，跳转歌词页
    }

    /**
     * 长按唱片显示歌曲封面
     */
    public void longClickRecord(){

    }

}
