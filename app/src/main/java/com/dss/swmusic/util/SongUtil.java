package com.dss.swmusic.util;

import android.media.MediaPlayer;
import android.net.Uri;

import com.dss.swmusic.MyApplication;

import java.io.IOException;

/**
 * 单首歌曲操作的工具类
 */
public class SongUtil {
    /**
     * 音乐播放器
     */
    public static MediaPlayer player = new MediaPlayer();
    /**
     * 是否首次播放过了
     */
    public static boolean isPrepared = false;
    /**
     * 是否点击了播放
     */
    public static boolean isPlaying = false;

    /**
     * 播放音频的静态方法（只有第一次播放需要）
     * @param uri 音频的uri
     */
    public static void play( Uri uri){
        //重置音频文件，防止多次点击出错
        player.reset();
        //传入播放地址
        try {
            player.setDataSource(MyApplication.getContext(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //异步准备资源，防止卡顿
        player.prepareAsync();
        //音频监听，音频准备完毕后进行播放
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });

        //改变两个boolean值
        isPrepared = true;
        isPlaying = true;
    }


    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }

}
