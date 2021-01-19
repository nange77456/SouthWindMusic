package com.dss.swmusic.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dss.swmusic.R;
import com.dss.swmusic.adapter.LyricAdapter;
import com.dss.swmusic.entity.LyricItem;

import java.util.ArrayList;
import java.util.List;

public class LyricView extends FrameLayout {

    private final String TAG = "LyricView";

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<LyricItem> lyrics;
    private LyricAdapter adapter = new LyricAdapter(null);

    /**1
     * 当前播放到第几句歌词
     */
    private int curIndex = 0;

    /**
     * 子项的高度
     */
    private int itemHeight;

    /**
     * Header View 的高度
     */
    private int headerHeight;

    /**
     * 能自动滚动的延迟时间
     */
    private Long canAutoScrollDelay = 2000L;

    /**
     * 能自动滚动的时间（只要大于这个才可以）
     */
    private Long canAutoScrollTime = 0L;

    /**
     * 能否自动滚动的标记
     */
    private boolean canAutoScroll = true;

    /**
     * 是否已经设置歌词
     */
    private boolean isSetLyric = false;

    /**
     * 点击事件监听
     */
    private OnClickListener onClickListener;

    private int shouldScrollToIndex = 0;

    public LyricView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        recyclerView = new RecyclerView(context);
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(recyclerView);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                switch (ev.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        canAutoScroll(false);
//                        downY = ev.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
//                        Log.e("tag","action move");
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
//                        upY = ev.getY();
//                        float dy = downY-upY;
//                        scrollY += dy;
                        canAutoScroll(true);
//                        Log.e("tag","action up/cancel");
                        break;
                }
                return false;
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if(onClickListener != null){
                    onClickListener.onClick(LyricView.this);
                }
            }
        });
    }


    /**
     * 获取RecyclerView一行歌词的高度
     */
    private void getItemHeight(){
        View item = LayoutInflater.from(getContext()).inflate(R.layout.item_lyric_inline,this,false);
        item.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        itemHeight = item.getMeasuredHeight();
//        Log.e("tag", "itemHeight = "+itemHeight);
        scrollToIndex(shouldScrollToIndex);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),getMeasuredWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        Log.e("tag","hhhhhhhhhhhhhhhhhh");
        View headerView = new View(getContext());
        headerHeight = getHeight()/2;
//        Log.e("tag","header height = "+headerHeight);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(getWidth(),getHeight()/2));
//        headerView.setBackgroundColor(Color.GREEN);
        adapter.addHeaderView(headerView);
        View footerView = new View(getContext());
        footerView.setLayoutParams(new ViewGroup.LayoutParams(getWidth(),getHeight()/2));
        adapter.addFooterView(footerView);

        headerView.setOnClickListener(v -> {
            if(onClickListener != null){
                onClickListener.onClick(LyricView.this);
            }
        });
        footerView.setOnClickListener(v ->{
            if(onClickListener != null){
                onClickListener.onClick(LyricView.this);
            }
        });

        getItemHeight();
    }

    public void setLyrics(List<LyricItem> lyrics) {
        this.lyrics = lyrics;
        adapter.setNewInstance(lyrics);
        isSetLyric = true;
    }

    public void setLyrics(String originalLyrics){
        this.lyrics = stringToList(originalLyrics);
        adapter.setNewInstance(lyrics);
        isSetLyric = true;
    }

    /**
     * 设置点击监听
     * @param onClickListener
     */
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    /**
     * 设置当前歌曲播放的时间
     * @param millisecond
     */
    public void setCurTime(int millisecond){
        if(!isSetLyric){
            return;
        }
        int i;
        for(i=0;i<lyrics.size();++i){
            int time = lyrics.get(i).getTime();
            if(millisecond < time){
                curIndex = Math.max(i - 1, 0);
                break;
            }
            if(millisecond == time){
                curIndex = i;
                break;
            }
        }
        if(i == lyrics.size()){
            curIndex = lyrics.size()-1;
        }
        Log.e(TAG, "setCurTime: time = "+millisecond+" , index = "+curIndex+" , "+lyrics.get(curIndex).getLyric() );
        // 设置高亮
        setHighLight();
        // 将歌词滚到中央
        if(isCanAutoScroll()){
            scrollToIndex(curIndex);
        }
    }

    /**
     * 设置是否能自动滚动
     * @param canOrNot
     */
    private void canAutoScroll(boolean canOrNot){
        if(canOrNot){
            canAutoScrollTime = System.currentTimeMillis()+canAutoScrollDelay;
        }else{
            canAutoScrollTime = Long.MAX_VALUE;
        }
    }

    /**
     * 判断是否能自动滚动
     * @return
     */
    private boolean isCanAutoScroll(){
        return System.currentTimeMillis() > canAutoScrollTime;
    }

    /**
     * 设置高亮
     */
    private void setHighLight(){
        adapter.setHighLightIndex(curIndex);
    }

    /**
     * 滚动到第 index 个歌词
     * @param index
     */
    public void scrollToIndex(int index){
//        Log.e("tag","scroll to "+index);
        if(itemHeight != 0){
            RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getContext()){

                @Override
                public int calculateDyToMakeVisible(View view, int snapPreference) {
                    return super.calculateDyToMakeVisible(view, snapPreference) + headerHeight;
                }

                @Override
                protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_START;
                }

                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return 0.7f;
                }
            };
            smoothScroller.setTargetPosition(index+1);
            layoutManager.startSmoothScroll(smoothScroller);
        }else{
            shouldScrollToIndex = index;
        }
    }

    /**
     * 滚动到指定的时间
     * @param millisecond 毫秒值
     */
    public void scrollToTime(int millisecond){
//        Log.e("tag","time = "+millisecond);
        for(int i=0;i<lyrics.size();++i){
            int time = lyrics.get(i).getTime();
            if(millisecond < time){
//                Log.e("tag","index = "+(i-1));
                scrollToIndex(i-1);
                break;
            }
            if(millisecond == time){
//                Log.e("tag","index = "+i);
                scrollToIndex(i);
                break;
            }
        }
    }

    public List<LyricItem> stringToList(String originalLyric){
        List<LyricItem> lyrics = new ArrayList<>();
        String[] a = originalLyric.split("\\n");
        for(String line:a){
            String[] b = line.substring(1).split("]");
            String c = b.length>1? b[1]:"";
            LyricItem lyric = new LyricItem(convertTime(b[0]),c);
//            Log.e("tag","yyyy: "+lyric);

            lyrics.add(lyric);
        }

        return lyrics;
    }

    public int convertTime(String timeStr){
        String[] a = timeStr.split(":");
        int minute = Integer.parseInt(a[0]);
        String[] b = a[1].split("\\.");
        int second = Integer.parseInt(b[0]);
        int millisecond = Integer.parseInt(b[1]);
        return minute*60*1000+second*1000+millisecond;
    }
}
