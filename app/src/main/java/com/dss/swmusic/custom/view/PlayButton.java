package com.dss.swmusic.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dss.swmusic.R;

public class PlayButton extends androidx.appcompat.widget.AppCompatImageView{

    private boolean isPlay = true;

    public PlayButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        showPlay();
    }


    public void showPlay(){
        Glide.with(this)
                .load(R.drawable.ic_play_play_activity)
                .into(this);
        isPlay = true;
    }

    public void showStop(){
        Glide.with(this)
                .load(R.drawable.ic_stop_play_activity)
                .into(this);
        isPlay = false;
    }



}
