package com.dss.swmusic.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import androidx.core.view.ViewConfigurationCompat;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by guolin on 16/1/12.
 */
public class GoonViewPager<T extends View> extends ViewGroup {

    private final String TAG = "GoonViewPager";

    /**
     * 用于完成滚动操作的实例
     */
    private Scroller mScroller;

    /**
     * 判定为拖动的最小移动像素数
     */
    private int mTouchSlop;

    /**
     * 手机按下时的屏幕坐标
     */
    private float mXDown;

    /**
     * 按下时的时间
     */
    private long mXDownTime;

    /**
     * 手机当时所处的屏幕坐标
     */
    private float mXMove;

    /**
     * 上次触发ACTION_MOVE事件时的屏幕坐标
     */
    private float mXLastMove;

    /**
     * 界面可滚动的左边界
     */
    private int leftBorder;

    /**
     * 界面可滚动的右边界
     */
    private int rightBorder;

    /**
     * 当前位置
     */
    private int curPosition = 0;
    /**
     * 目标位置
     */
    private int targetPosition = 0;

    /**
     * 是否正在滑动
     */
    private boolean isScroll = false;

    private OnScrollListener onScrollListener;

    private Adapter<T> adapter;

    private LinkedList<T> viewList = new LinkedList<T>();

    private final int fillingSpeed = 1500;

    private final int scrollDuration = 400;





    public GoonViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 第一步，创建Scroller的实例
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    public void setAdapter(Adapter<T> adapter) {
        this.adapter = adapter;
        for (int i = 0; i < 3; ++i) {
            T view = adapter.createView();
            view.setClickable(true);
            viewList.add(view);
            addView(view);
        }
        adapter.initData(viewList);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 为ScrollerLayout中的每一个子控件测量大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            Log.e("tag", "onLayout");
            int childCount = getChildCount();
//            for (int i = 0; i < childCount; i++) {
//                View childView = getChildAt(i);
//                // 为ScrollerLayout中的每一个子控件在水平方向上进行布局
//                childView.layout(i * childView.getMeasuredWidth(), 0, (i + 1) * childView.getMeasuredWidth(), childView.getMeasuredHeight());
//            }
//            // 初始化左右边界值
//            leftBorder = getChildAt(0).getLeft();
//            rightBorder = getChildAt(getChildCount() - 1).getRight();
            if (childCount != 3) {
                return;
            }
            View child1 = getChildAt(0);
            View child2 = getChildAt(1);
            View child3 = getChildAt(2);
            child1.layout(-getMeasuredWidth(), 0, 0, getMeasuredHeight());
            child2.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
            child3.layout(getMeasuredWidth(), 0, getMeasuredWidth() * 2, getMeasuredHeight());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mXDownTime = System.currentTimeMillis();
                mXLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (diff > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.e(TAG, "onTouchEvent: action = " + event.getAction());
//        if(gestureDetector.onTouchEvent(event)){
//            return true;
//        }
        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:
                onScroll();
                mXMove = event.getRawX();
                int scrolledX = (int) (mXLastMove - mXMove);

                scrollBy(scrolledX, 0);
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"action_up");
                // 计算速度
                long dTime = System.currentTimeMillis()-mXDownTime;
                float dx = event.getRawX()-mXDown;
                float speed = dx/dTime * 1000;
                Log.e(TAG, "onTouchEvent: speed = "+speed );
                if(speed < -fillingSpeed){
                    // 向右滑
                    moveToTarget(curPosition+1);
                }else if(speed > fillingSpeed){
                    // 向左滑
                    moveToTarget(curPosition-1);
                }else{
                    // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                    int targetIndex = 0;
                    if (getScrollX() >= 0) {
                        targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                    } else {
                        targetIndex = -(-getScrollX() + getWidth() / 2) / getWidth();
                    }
                    moveToTarget(targetIndex);
                }


                break;
        }
        return super.onTouchEvent(event);
    }

    private void moveToTarget(int targetIndex){
        targetPosition = targetIndex;
        int dx = targetIndex * getWidth() - getScrollX();
        Log.e(TAG, "onTouchEvent: targetIndex = " + targetIndex);
        // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
        mScroller.startScroll(getScrollX(), 0, dx, 0,scrollDuration);
        invalidate();
    }

    /**
     * 滑动到下一页
     */
    public void scrollToNext() {
        onScroll();
        moveToTarget(curPosition+1);
    }

    /**
     * 滑动到上一页
     */
    public void scrollToLast(){
        onScroll();
        moveToTarget(curPosition-1);
    }

    private void onScroll(){
        if(isScroll == false){
            isScroll = true;
            if(onScrollListener != null){
                onScrollListener.startScroll();
            }
        }
    }

    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
            if (mScroller.isFinished()) {

                if (targetPosition > curPosition) {
                    onToNextCompletion();
                } else if (targetPosition < curPosition) {
                    onToLastCompletion();
                }else{
                    onToCurCompletion();
                }
                curPosition = targetPosition;


            }
        }
    }

    public T getCurView(){
        return viewList.get(1);
    }

    public T getLastView(){ return viewList.get(0);}

    public T getNextView(){ return viewList.get(2);}

    private void onToCurCompletion(){
        isScroll = false;
        if(onScrollListener != null){
            onScrollListener.endScroll(curPosition,targetPosition);
        }
    }

    private void onToNextCompletion() {
        Log.e(TAG, "onToNextCompletion: ");
        isScroll = false;
        T firstView = viewList.removeFirst();
        viewList.addLast(firstView);

        if(onScrollListener != null){
            onScrollListener.endScroll(curPosition,targetPosition);
        }

        adapter.setNextData(firstView);
        firstView.layout((targetPosition+1)*getWidth(),0,(targetPosition+2)*getWidth(),getHeight());


    }

    private void onToLastCompletion() {
        Log.e(TAG, "onToLastCompletion: ");
        isScroll = false;
        T lastView = viewList.removeLast();
        viewList.addFirst(lastView);

        if(onScrollListener != null){
            onScrollListener.endScroll(curPosition,targetPosition);
        }

        adapter.setLastData(lastView);
        lastView.layout((targetPosition-1)*getWidth(),0,targetPosition*getWidth(),getHeight());


    }

    interface OnScrollListener{

        void startScroll();

        void endScroll(int oldPosition,int newPosition);
    }

    interface Adapter<T extends View> {

        T createView();

        void initData(List<T> viewList);

        void setNextData(T view);

        void setLastData(T view);
    }

}

