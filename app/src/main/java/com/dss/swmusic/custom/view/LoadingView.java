package com.dss.swmusic.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dss.swmusic.R;

public class LoadingView extends FrameLayout {

    private ImageView loadingImg;
    private TextView loadingTextView;

    public LoadingView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_view_loading,this);
        loadingImg = view.findViewById(R.id.loadingImg);
        loadingTextView = view.findViewById(R.id.loadingTextView);

        Glide.with(loadingImg)
                .load(R.drawable.loading)
                .into(loadingImg);
    }

    public void noData(){
        loadingImg.setVisibility(INVISIBLE);
        loadingTextView.setText("没有数据");
    }

    public void loading(){
        loadingImg.setVisibility(VISIBLE);
        loadingTextView.setText("努力加载中...");
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }
}
