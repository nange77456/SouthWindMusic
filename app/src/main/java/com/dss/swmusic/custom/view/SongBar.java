package com.dss.swmusic.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dss.swmusic.R;

public class SongBar extends FrameLayout {

    public SongBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_view_song_bar,this,true);


    }

}
