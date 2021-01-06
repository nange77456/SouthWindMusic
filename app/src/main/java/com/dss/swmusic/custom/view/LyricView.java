package com.dss.swmusic.custom.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dss.swmusic.R;
import com.dss.swmusic.adapter.LyricAdapter;
import com.dss.swmusic.entity.Lyric;

import java.util.ArrayList;
import java.util.List;

public class LyricView extends FrameLayout {
    RecyclerView lyricRecycler;
    List<Lyric> lyrics;
    LyricAdapter adapter = new LyricAdapter(null);

    private Context context;

    public LyricView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.custom_view_lyric,this);
        lyricRecycler = view.findViewById(R.id.lyricRecycler);
        lyricRecycler.setAdapter(adapter);
        lyricRecycler.setLayoutManager(new LinearLayoutManager(context));


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                ViewGroup headerContainer = new RelativeLayout(context);
//                View headerView = new View(context);
//                headerView.setLayoutParams(new ViewGroup.LayoutParams(lyricRecycler.getWidth(),600));
//                headerView.setBackgroundColor(Color.BLUE);
//                headerContainer.addView(headerView);
//                Log.e("tag","height = "+getHeight()/2);
//                Log.e("tag","height2 = "+view.getHeight());
//                View footerView = new View(context);
//                footerView.setLayoutParams(new LayoutParams(lyricRecycler.getWidth(),getHeight()/2));

                Log.e("tag","runnnnnnnnnnnnnnnn");



//                adapter.addHeaderView(headerView);
//                Log.e("tag","headerLayout height = "+adapter.getHeaderLayout().getHeight());
//                adapter.addFooterView(footerView);
            }
        });




    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        Log.e("tag","hhhhhhhhhhhhhhhhhh");
        View headerView = new View(getContext());
        headerView.setLayoutParams(new ViewGroup.LayoutParams(getWidth(),getHeight()/2));
//        headerView.setBackgroundColor(Color.GREEN);
        adapter.addHeaderView(headerView);
        View footerView = new View(getContext());
        footerView.setLayoutParams(new ViewGroup.LayoutParams(getWidth(),getHeight()/2));
        adapter.addFooterView(footerView);
    }

    public void setLyrics(List<Lyric> lyrics) {
        this.lyrics = lyrics;
        adapter.setNewInstance(lyrics);
    }

    public void setLyrics(String originalLyrics){
        this.lyrics = stringToList(originalLyrics);
        adapter.setNewInstance(lyrics);


    }

    public List<Lyric> stringToList(String originalLyric){
        List<Lyric> lyrics = new ArrayList<>();
        String[] a = originalLyric.split("\\n");
        for(String line:a){
            String[] b = line.substring(1).split("]");
            String c = b.length>1? b[1]:"";
            Lyric lyric = new Lyric(convertTime(b[0]),c);
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
