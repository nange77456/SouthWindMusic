package com.dss.swmusic.custom.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dss.swmusic.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongBar extends FrameLayout {

    private CircleImageView albumPic;
    private TextView songNameTextView;
    private TextView songArtistTextView;
    private ProgressPlayButton playButton;
    private View playListBtn;

    public SongBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_view_song_bar,this,true);
        albumPic = view.findViewById(R.id.circleImageView);
        songNameTextView = view.findViewById(R.id.songName);
        songArtistTextView = view.findViewById(R.id.songArtist);
        playButton = view.findViewById(R.id.progressPlayButton);
        playListBtn = view.findViewById(R.id.playList);

    }

    public void setOnPlayBtnClickListener(OnClickListener onClickListener){
        playButton.setOnClickListener(onClickListener);
    }

    public void setOnPlayListBtnClickListener(OnClickListener listener){
        playListBtn.setOnClickListener(listener);
    }

    public void setProgressPlayBtn(boolean isPlaying){
        playButton.setIsPlaying(isPlaying);
    }

    public void setProgressPlayBtn(boolean isPlaying,float progress){
        playButton.setIsPlaying(isPlaying);
        playButton.setRate(progress);
    }

    public void setAlbumPic(String url){
        if(url == null || url.equals("")){
            Glide.with(this)
            .load(R.drawable.play_list_default_cover)
            .into(albumPic);
        }else{
            Glide.with(this)
                    .load(url)
                    .into(albumPic);
        }
    }

    // TODO 神奇的闪退bug
    public void setAlbumPic(Activity activity,String url){
        if(activity == null || activity.isFinishing()){
            return;
        }
        if(url == null || url.equals("")){
            Glide.with(activity)
                    .load(R.drawable.play_list_default_cover)
                    .into(albumPic);
        }else{
            Glide.with(activity)
                    .load(url)
                    .into(albumPic);
        }
    }

    public void setSongName(String str){
        songNameTextView.setText(str);
    }

    public void setArtistName(String str){
        songArtistTextView.setText(str);
    }

}
