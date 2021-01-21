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
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainTabLayout extends LinearLayout {

    private ViewPager viewPager = null;

    /**
     * ViewPager 的当前页
     */
    private int curPageIndex = 0;

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

    /**
     * 是否开启监听滚动
     */
    private boolean listenerScrollFlag = true;

    /**
     * 点击同一项的监听
     */
    private OnClickCurItemListener onClickCurItemListener = null;

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
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        ((LayoutParams)params).weight = 1;
        ((LayoutParams)params).width = LayoutParams.MATCH_PARENT;
        MainTabItem item = (MainTabItem) child;
        tabItems.add(item);
        if(temp == 0){
            item.setTextColor(selectTextColor);
            item.setTextSize(selectTextSize);
        }else{
            item.setTextColor(unSelectTextColor);
            item.setTextSize(unSelectTextSize);
        }
        item.setOnClickListener(v->{
            if(viewPager != null){
                int choose = tabItems.indexOf(item);
                if(choose == viewPager.getCurrentItem() && onClickCurItemListener != null){
                    onClickCurItemListener.clickCurItem(choose);
                }
                viewPager.setCurrentItem(choose);
                listenerScrollFlag = false;
                setCurrentSelect(choose);
//                listenerScrollFlag = true;
            }
        });
        temp++;
        super.addView(child, index, params);
    }

    public void setCurrentSelect(int index){
        if(index < 0 || index >= tabItems.size()){
            return;
        }
        if(viewPager != null){
            viewPager.setCurrentItem(index);
        }
        for(int i=0;i<tabItems.size();++i){
            MainTabItem item = tabItems.get(i);
            if(i == index){
                item.setTextSize(selectTextSize);
                item.setTextColor(selectTextColor);
            }else{
                item.setTextSize(unSelectTextSize);
                item.setTextColor(unSelectTextColor);
            }
        }
    }

    /**
     * 设置ViewPager
     * @param viewPager
     */
    public void setViewPager(ViewPager viewPager){
        this.viewPager = viewPager;

        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean scrollFlag = false;

            int cur;
            int next;

            float lastPositionOffset = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
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
//                        Log.e("MeDebug","向右滑,next="+next);
                    }else{
                        next = curPageIndex -1;
//                        Log.e("MeDebug","向左滑,next="+next);
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
                curPageIndex = position;
//                Log.e("MeDebug","position = "+position);
//                scrollFlag = false;
//                flag2 = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager2.SCROLL_STATE_IDLE && !listenerScrollFlag){
                    // 别问为什么，反正你回头看这行代码肯定想不明白，不要改就行了
                    listenerScrollFlag = true;
                }
                if(state == ViewPager2.SCROLL_STATE_IDLE){
                    setCurrentSelect(curPageIndex);
                }
            }
        });
//        this.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//
//            boolean scrollFlag = false;
//
//            int cur;
//            int next;
//
//            float lastPositionOffset = 0;
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
////                Log.e("MeDebug","onPageScrolled "+position+","+positionOffset+","+positionOffsetPixels);
//
////                Log.e("MyDebug","positio="+position+",curPage="+curPageIndex);
//
//                // 这里为什么这么写？别想了，反正就这样
//                if(Math.abs(lastPositionOffset-positionOffset) > 0.8 || positionOffset == 0){
//                    scrollFlag = false;
//                }
//
//                if(!scrollFlag && positionOffset != 0){
//                    cur = curPageIndex;
//                    if(positionOffset < (1-positionOffset)){
//                        // 向右滑
//                        next = curPageIndex+1;
//                        Log.e("MeDebug","向右滑,next="+next);
//                    }else{
//                        next = curPageIndex -1;
//                        Log.e("MeDebug","向左滑,next="+next);
//                    }
//                    scrollFlag = true;
//                }
//                if(scrollFlag){
//                    if(cur < next)
//                        onScrolled(cur,next,positionOffset);
//                    else
//                        onScrolled(cur,next,1-positionOffset);
//                }
//
//                lastPositionOffset = positionOffset;
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                curPageIndex = position;
////                Log.e("MeDebug","position = "+position);
////                scrollFlag = false;
////                flag2 = false;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
//                if(state == ViewPager2.SCROLL_STATE_IDLE && !listenerScrollFlag){
//                    // 别问为什么，反正你回头看这行代码肯定想不明白，不要改就行了
//                    listenerScrollFlag = true;
//                }
//                if(state == ViewPager2.SCROLL_STATE_IDLE){
//                    setCurrentSelect(curPageIndex);
//                }
//            }
//        });


    }

    /**
     * 设置点击当前项的监听
     * @param onClickCurItemListener
     */
    public void setOnClickCurItemListener(OnClickCurItemListener onClickCurItemListener) {
        this.onClickCurItemListener = onClickCurItemListener;
    }

    private void onScrolled(int from, int to, float offset){
//        Log.e("MyDebug","test");
        if(!listenerScrollFlag){
            return;
        }
//        Log.e("MyDebug","from "+from+" ,to "+to+" ,offset"+offset);
        if(from < 0){
            from = 0;
        }
        if(from >= tabItems.size()){
            from = tabItems.size()-1;
        }
        if(to < 0){
            to = 0;
        }
        if(to >= tabItems.size()){
            to = tabItems.size()-1;
        }
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

    public interface OnClickCurItemListener{
        void clickCurItem(int index);
    }

}
