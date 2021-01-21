package com.dss.swmusic.util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.dss.swmusic.R;
import com.dss.swmusic.cache.RecentPlayCache;
import com.dss.swmusic.custom.dialog.PlayListDialog;
import com.dss.swmusic.custom.view.SongBar;
import com.dss.swmusic.entity.PlayerSong;
import com.dss.swmusic.me.PlayActivity;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SongBarHelper {

    private SongPlayer.OnPlayListener onPlayListener;

    private RecentPlayCache cache = new RecentPlayCache();

    public void setSongBar(Activity activity,SongBar songBar){
        onPlayListener = new SongPlayer.SimpleOnPlayListener(){

            @Override
            public void onStart() {
                if(songBar.getVisibility() == View.GONE){
                    songBar.setVisibility(View.VISIBLE);
                }
                songBar.setProgressPlayBtn(true,0);
                PlayerSong song = SongPlayer.getCurSong();
                songBar.setAlbumPic(activity,song.getAlbums().getPicUrl());
                songBar.setSongName(song.getName());
                songBar.setArtistName(ExtensionKt.toNiceString(song.getArtists()));
            }

            @Override
            public void onPlay() {
                songBar.setProgressPlayBtn(true);
            }

            @Override
            public void onPlaying(int mesc) {
                float rate = mesc*1f/SongPlayer.getDuration();
                songBar.setProgressPlayBtn(true,rate);
            }

            @Override
            public void onStop() {
                songBar.setProgressPlayBtn(false);
            }
        };
        SongPlayer.addOnPlayListener(onPlayListener);

        setOnClickListener(activity,songBar);

        // 如果播放器已有预备播放的歌曲，则设置基本数据
        if(SongPlayer.getCurSong() != null){
            setData(songBar);
            songBar.setVisibility(View.VISIBLE);
        }else{
            // 尝试从缓存中读取数据
            cache.getPlayListCache(songs -> {
                if(songs == null){
                    // 如果无数据则隐藏
                    songBar.setVisibility(View.GONE);
                }else{
                    // 如果有数据则设置并显示
                    cache.getIndexCache(integer -> {
                        if(integer == null){
                            integer = 0;
                        }
                        SongPlayer.setPlayList(songs,integer);
                        setData(songBar);
                        songBar.setVisibility(View.VISIBLE);
                        return null;
                    });
                }
                return null;
            });
        }


    }

    private void setOnClickListener(Activity activity,SongBar songBar){
        // 设置点击事件
        songBar.setOnClickListener((v)->{
            Intent intent = new Intent(activity, PlayActivity.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_bottom_in,R.anim.none);
        });
        songBar.setOnPlayBtnClickListener((v)->{
            if(SongPlayer.isPlaying()){
                SongPlayer.pause();
            }else{
                SongPlayer.goon();
            }
        });
        songBar.setOnPlayListBtnClickListener(v -> {
            PlayListDialog dialog = new PlayListDialog(activity);
            dialog.show();
        });
    }

    private void setData(SongBar songBar){
        // 初始化数据
        PlayerSong song = SongPlayer.getCurSong();
        songBar.setAlbumPic(song.getAlbums().getPicUrl());
        songBar.setSongName(song.getName());
        songBar.setArtistName(ExtensionKt.toNiceString(song.getArtists()));
        float rate = SongPlayer.getCurrentPosition()*1f/SongPlayer.getDuration();
        songBar.setProgressPlayBtn(SongPlayer.isPlaying(),rate);
    }

    public void release(){
        SongPlayer.removeOnPlayListener(onPlayListener);
    }

}
