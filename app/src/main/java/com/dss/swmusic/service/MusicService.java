package com.dss.swmusic.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.dss.swmusic.R;
import com.dss.swmusic.me.PlayActivity;

public class MusicService extends Service {
    public static final int NOTIFICATION_ID = 1;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

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
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomBigContentView(notificationBigLayout)
                .setCustomContentView(notificationSmallLayout)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        //启动前台服务
        startForeground(NOTIFICATION_ID,notification);
//        Log.e("tag","启动了前台服务");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
