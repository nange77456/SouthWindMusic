package com.dss.swmusic.custom.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainTabLayout extends LinearLayout {

    private ViewPager2 viewPager2 = null;

    /**
     * ViewPager 的当前页
     */
    private int curPageIndex = 0;

    /**
     * 上一个 positionOffset，用来判断滑动方向
     */
    private float lastPositionOffset = 0;

    /**
     * 选中状态的文字大小，单位为sp
     */
    private float selectTextSize = 20;

    /**
     * 未选中状态的文字大小，单位为sp
     */
    private float unSelectTextSize = 18;

    /**
     * 选中的文字颜色
     */
    private int selectTextColor = Color.BLACK;

    /**
     * 未选中的文字颜色
     */
    private int unSelectTextColor = Color.GRAY;

    // 什么用？写了也看不懂
    private int temp = 0;


    /**
     * 子项
     */
    private List<MainTabItem> tabItems = new ArrayList<>();

    public MainTabLayout(Context context) {
        super(context);
    }

    public MainTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);

    }

    public MainTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for(int i=0;i<getChildCount();++i){
            MainTabItem child = (MainTabItem) getChildAt(i);
            tabItems.add(child);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        ((LayoutParams)params).weight = 1;
        ((LayoutParams)params).width = LayoutParams.MATCH_PARENT;
        MainTabItem item = (MainTabItem) child;
        if(temp == 0){
            item.setTextColor(selectTextColor);
            item.setTextSize(selectTextSize);
        }else{
            item.setTextColor(unSelectTextColor);
            item.setTextSize(unSelectTextSize);
        }
        temp++;
        super.addView(child, index, params);
    }

    /**
     * 设置ViewPager
     * @param viewPager2
     */
    public void setViewPager2(ViewPager2 viewPager2){
        this.viewPager2 = viewPager2;

        this.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            boolean scrollFlag = false;

            int cur;
            int next;

            float lastPositionOffset = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                Log.e("MeDebug","onPageScrolled "+position+","+positionOffset+","+positionOffsetPixels);

//                Log.e("MyDebug","positio="+position+",curPage="+curPageIndex);

                // 这里为什么这么写？别想了，反正就这样
                if(Math.abs(lastPositionOffset-positionOffset) > 0.8 || positionOffset == 0){
                    scrollFlag = false;
                }

                if(!scrollFlag && positionOffset != 0){
                    cur = curPageIndex;
                    if(positionOffset < (1-positionOffset)){
                        // 向右滑
                        next = curPageIndex+1;
                        Log.e("MeDebug","向右滑,next="+next);
                    }else{
                        next = curPageIndex -1;
                        Log.e("MeDebug","向左滑,next="+next);
                    }
                    scrollFlag = true;
                }
                if(scrollFlag){
                    if(cur < next)
                        onScrolled(cur,next,positionOffset);
                    else
                        onScrolled(cur,next,1-positionOffset);
                }

                lastPositionOffset = positionOffset;

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                curPageIndex = position;
//                Log.e("MeDebug","position = "+position);
//                scrollFlag = false;
//                flag2 = false;
            }


        });


    }

    private void onScrolled(int from,int to,float offset){

//        Log.e("MyDebug","from "+from+" ,to "+to+" ,offset"+offset);

        MainTabItem curItem = tabItems.get(from);
        MainTabItem nextItem = tabItems.get(to);

        float textSizeDiffer = (selectTextSize-unSelectTextSize)*offset;

        curItem.setTextSize(TypedValue.COMPLEX_UNIT_SP,selectTextSize-textSizeDiffer);
        nextItem.setTextSize(TypedValue.COMPLEX_UNIT_SP,unSelectTextSize+textSizeDiffer);

        if(offset > 0.5){
            curItem.setTextColor(unSelectTextColor);
            nextItem.setTextColor(selectTextColor);
        }else{
            curItem.setTextColor(selectTextColor);
            nextItem.setTextColor(unSelectTextColor);
        }

    }



}
