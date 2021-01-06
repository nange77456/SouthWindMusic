package com.dss.swmusic.custom.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 自定义view，歌曲进度条
 * 高度至少给30
 */
public class ProgressBar extends View {
    /**
     * 播放进度比率
     */
    private float rate = 0.5f;
    /**
     * 画笔
     */
    private Paint paint = new Paint();

    /**
     * 进度条颜色-左
     */
    private int firstColor = Color.parseColor("#ffffff");
    /**
     * 进度条颜色-右
     */
    private int secondColor = Color.parseColor("#66ffffff");
    /**
     * 指示进度的小圆点的颜色
     */
    private int thirdColor = Color.parseColor("#ffffff");

    /**
     * 指示进度的小圆点的小半径
     */
    private float smallRadius = 10;
    /**
     * 指示进度的小圆点的大半径
     */
    private float bigRadius = 20;
    /**
     * 指示进度的小圆点的变化的半径
     */
    private float radius = smallRadius;

    private float padding = 10;

    private OnRateChangeListener onRateChangeListener;

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(5);

        paint.setColor(firstColor);
        canvas.drawLine(padding,getHeight()/2,getWidth()*rate,getHeight()/2,paint);
        paint.setColor(secondColor);
        canvas.drawLine(padding+getWidth()*rate,getHeight()/2,getWidth()-padding,getHeight()/2,paint);
        paint.setColor(thirdColor);
        canvas.drawCircle((getWidth()-padding*2)*rate+padding,getHeight()/2,radius,paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                Log.e("tag","UP");
                if(onRateChangeListener != null){
                    onRateChangeListener.onRateChange( rate);
                }
                stopAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("tag","MOVE");
                rate = event.getX()/getWidth();
                invalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                Log.e("tag","DOWN");
                rate = event.getX()/getWidth();
                invalidate();

                startAnimation();
                break;

        }


        return true;
    }

    private void startAnimation(){
        ValueAnimator pointAnimator = ValueAnimator.ofFloat(smallRadius,bigRadius);
        pointAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                radius = value;
                invalidate();

            }
        });

        pointAnimator.setDuration(100);
        pointAnimator.start();
    }

    private void stopAnimation(){
        ValueAnimator pointAnimator = ValueAnimator.ofFloat(bigRadius,smallRadius);
        pointAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                radius = value;
                invalidate();
            }
        });

        pointAnimator.setDuration(100);
        pointAnimator.start();
    }


    public void setRate(float rate){
        this.rate = rate;
        invalidate();
    }

    public float getRate(){return rate;}

    public void setOnRateChangeListener(OnRateChangeListener onRateChangeListener) {
        this.onRateChangeListener = onRateChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public static interface OnRateChangeListener{
        void onRateChange(float rate);
    }

}
