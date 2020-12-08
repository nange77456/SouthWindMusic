package com.dss.swmusic.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.dss.swmusic.R;
import com.dss.swmusic.entity.LocalSong;
import com.dss.swmusic.me.PlayActivity;
import com.dss.swmusic.util.SongUtil;

public class MusicService extends Service {
    public static final int NOTIFICATION_ID = 1;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //创建通知
        createNotification();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocalSong song = (LocalSong) intent.getSerializableExtra("clickedSong");
        //播放
        playService(song);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 创建通知的前台服务
     */
    private void createNotification(){
        //创建通知渠道，channel
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("no_mean","前台通知服务", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        //设置通知的点击事件，pendingIntent
        Intent intent = new Intent(this, PlayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        //创建通知的视图，remoteViews
        RemoteViews notificationBigLayout = new RemoteViews(getPackageName(),R.layout.notification_big);
        RemoteViews notificationSmallLayout = new RemoteViews(getPackageName(),R.layout.notification_small);

        notificationBigLayout.setImageViewResource(R.id.cover,R.mipmap.ic_launcher);
        notificationBigLayout.setImageViewResource(R.id.like,R.drawable.ic_heart_filled);
        notificationBigLayout.setImageViewResource(R.id.previous,R.drawable.ic_previous);
        notificationBigLayout.setImageViewResource(R.id.play,R.drawable.ic_play);
        notificationBigLayout.setImageViewResource(R.id.next,R.drawable.ic_next);
        notificationBigLayout.setImageViewResource(R.id.cancel,R.drawable.ic_cancel);

        notificationSmallLayout.setImageViewResource(R.id.cover,R.mipmap.ic_launcher);
        notificationSmallLayout.setImageViewResource(R.id.play,R.drawable.ic_play);
        notificationSmallLayout.setImageViewResource(R.id.next,R.drawable.ic_next);
        notificationSmallLayout.setImageViewResource(R.id.cancel,R.drawable.ic_cancel);


        //用上面的通知渠道和pendingIntent创建通知
        Notification notification = new NotificationCompat.Builder(this,"no_mean")
                .setSmallIcon(R.drawable.ic_logo)
                .setCustomBigContentView(notificationBigLayout)
                .setCustomContentView(notificationSmallLayout)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        //启动前台服务
        startForeground(NOTIFICATION_ID,notification);

    }

    /**
     * 播放功能放在服务里
     * @param song 需要被播放的本地音乐
     */
    private void playService(LocalSong song){
        if(!SongUtil.isPrepared){
            //1. 首次播放
            SongUtil.play(Uri.parse(song.getUriStr()));
        }else {
            if(!SongUtil.isPlaying){
                //2. 暂停后的播放
                SongUtil.player.start();
                SongUtil.isPlaying = true;
            }else {
                //3. 暂停
                SongUtil.player.pause();
                SongUtil.isPlaying = false;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
