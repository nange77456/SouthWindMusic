package com.dss.swmusic.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import java.io.IOException;

/**
 * 单首歌曲操作的工具类
 */
public class SongUtil {
    /**
     *
     */
    public static MediaPlayer player = new MediaPlayer();

    /**
     * 播放音频的静态方法
     * @param path 音频的地址
     */
    public static void play(String path){
        //重置音频文件，防止多次点击出错
        player.reset();
        //传入播放地址
        try {
            player.setDataSource(path);
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
