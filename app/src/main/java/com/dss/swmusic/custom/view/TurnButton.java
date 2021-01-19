package com.dss.swmusic.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dss.swmusic.R;
import com.dss.swmusic.util.SongPlayer;

public class TurnButton extends androidx.appcompat.widget.AppCompatImageView {

    private int turnType = SongPlayer.TurnType.SEQUENCE;

    private OnTurnTypeChangeListener onTurnTypeChangeListener;

    public TurnButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Glide.with(this)
                .load(R.drawable.ic_list_recycle)
                .into(this);

        setOnClickListener(v->{
            nextTurnType();
        });
    }

    public void setOnTurnTypeChangeListener(OnTurnTypeChangeListener onTurnTypeChangeListener) {
        this.onTurnTypeChangeListener = onTurnTypeChangeListener;
    }


    public void setTurn(int turnType) {
        this.turnType = turnType;
        switch (turnType) {
            case SongPlayer.TurnType.SEQUENCE:
                Glide.with(this)
                        .load(R.drawable.ic_list_recycle)
                        .into(this);
                break;
            case SongPlayer.TurnType.RANDOM:
                Glide.with(this)
                        .load(R.drawable.ic_turn_random)
                        .into(this);
                break;
            case SongPlayer.TurnType.REPEAT:
                Glide.with(this)
                        .load(R.drawable.ic_turn_repeat)
                        .into(this);
                break;
        }
        if(onTurnTypeChangeListener != null){
            onTurnTypeChangeListener.onChange(turnType);
        }
    }

    public void nextTurnType(){
        switch (turnType) {
            case SongPlayer.TurnType.SEQUENCE:
                turnType = SongPlayer.TurnType.RANDOM;
                break;
            case SongPlayer.TurnType.RANDOM:
                turnType = SongPlayer.TurnType.REPEAT;
                break;
            case SongPlayer.TurnType.REPEAT:
                turnType = SongPlayer.TurnType.SEQUENCE;
                break;
        }
        setTurn(turnType);
    }

    public static interface OnTurnTypeChangeListener{
        void onChange(int type);
    }
}
