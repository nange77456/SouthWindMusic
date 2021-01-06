package com.dss.swmusic.custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ProgressPlayButton extends View implements View.OnClickListener {
    /**
     * 播放进度比率
     */
    private float rate = 0.5f;

    private Paint paint = new Paint();
    private int color = Color.parseColor("#333333");
    private int color2 = Color.parseColor("#f50057");

    /**
     * 外正方形长和宽
     */
    private int width;
    /**
     * 弧形进度条外切矩形
     */
    private RectF oval;

    /**
     * 播放暂停标志
     */
    private boolean isPlaying;

    public ProgressPlayButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        oval = new RectF();

        setOnClickListener(this);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = Math.min(getMeasuredWidth(),getMeasuredHeight());
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(5);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        //画圆
        canvas.drawCircle(width/2f,width/2f,width/2f*0.9f,paint);
        //画三角形, 画平行线
        if(isPlaying){
            drawTwoLines(canvas);
        }else {
            drawTriangle(canvas);
        }
        //画圆弧
        paint.setColor(color2);
        oval.left = width*0.05f;
        oval.top = width*0.05f;
        oval.right = width*0.95f;
        oval.bottom = width*0.95f;
        canvas.drawArc(oval,-90,360*rate,false,paint);
    }

    /**
     * 画三角形
     * @param canvas
     */
    private void drawTriangle(Canvas canvas){
        //三角形三个点的坐标
        //x方向：6=2.5+1.5+2.0，100%=0.42+0.25+0.33
        //y方向：6=1.9+2.1+2.0，100%=0.32+0.35+0.33
        float x1=width*0.42f,y1=width*0.32f,x2=width*0.42f,y2=width*0.67f,x3=width*0.7f,y3=width*0.5f;

        paint.setColor(color);
        canvas.drawLine(x1,y1,x2,y2,paint);
        canvas.drawLine(x1,y1,x3,y3,paint);
        canvas.drawLine(x2,y2,x3,y3,paint);
    }

    /**
     * 画两条竖线
     * @param canvas
     */
    private void drawTwoLines(Canvas canvas){
        //两条竖线的四个点的坐标
        float x1=width*0.4f,y1=width*0.35f,x2=x1,y2=width*0.65f,x3=width*0.6f,y3=y1,x4=x3,y4=y2;

        paint.setColor(color2);
        canvas.drawLine(x1,y1,x2,y2,paint);
        canvas.drawLine(x3,y3,x4,y4,paint);
    }

    /**
     * 重设进度比率，并重新绘制
     * @param rate
     */
    public void setRate(float rate) {
        this.rate = rate;
        invalidate();
    }

    @Override
    public void onClick(View v) {
        isPlaying = !isPlaying;
        invalidate();
    }
}
