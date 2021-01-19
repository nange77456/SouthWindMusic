package com.dss.swmusic.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dss.swmusic.ActivityCollector;
import com.dss.swmusic.MainActivity;
import com.dss.swmusic.R;
import com.dss.swmusic.cache.RecentPlayCache;
import com.dss.swmusic.entity.PlayerSong;
import com.dss.swmusic.entity.Song;
import com.dss.swmusic.me.PlayActivity;
import com.dss.swmusic.util.SongPlayer;
import com.dss.swmusic.util.SongUtil;

public class MusicService extends Service {

    private static final String TAG = "MusicService";

    public static final int NOTIFICATION_ID = 1;
    private RemoteViews notificationBigLayout;
    private RemoteViews notificationSmallLayout;

    /**
     * 广播接收器的 action
     */
    public static final String CONTROL_INTENT_ACTION = "com.dss.swmusic.musicService.broadcastReceiver";

    /**
     * 广播的几个信号
     */
    public static final String SIGNAL_KEY = "control_signal";
    public static final int PLAY_SIGNAL = 0;
    public static final int NEXT_SIGNAL = 2;
    public static final int LAST_SIGNAL = 3;
    public static final int CANCEL_SIGNAL = 4;

    private SongPlayer.OnPlayListener onPlayListener = new SongPlayer.SimpleOnPlayListener() {
        @Override
        public void onStart() {
            PlayerSong song = SongPlayer.getCurSong();
            Log.e(TAG, "onStart");
            setRemoteView(song.getAlbums().getPicUrl(),song.getName(),song.getArtists().get(0).getName(),song.getAlbums().getName());
            setPlayButton(true);
        }

        @Override
        public void onPlaying(int mesc) {

        }

        @Override
        public void onStop() {
            Log.e(TAG, "onStop: " );
            setPlayButton(false);
            cacheRecentPlay();
        }

        @Override
        public void onNext() {

        }

        @Override
        public void onLast() {

        }

        @Override
        public void onGoon() {
            Log.e(TAG, "onGoon:");
            setPlayButton(true);
        }

        @Override
        public void onOver() {
            setPlayButton(false);
        }
    };
    private NotificationManager notificationManager;
    private Notification notification;
    private NotificationCompat.Builder notificationBuilder;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int signal = intent.getIntExtra(SIGNAL_KEY,PLAY_SIGNAL);
            switch (signal){
                case PLAY_SIGNAL:
                    if(SongPlayer.getPlayState() == SongPlayer.State.PLAYING){
                        SongPlayer.pause();
//                        setPlayButton(false);
                    }else{
                        SongPlayer.goon();
//                        setPlayButton(true);
                    }
                    break;
                case NEXT_SIGNAL:
                    SongPlayer.playNext();
                    break;
                case LAST_SIGNAL:
                    SongPlayer.playLast();
                    break;
                case CANCEL_SIGNAL:
                    ActivityCollector.INSTANCE.finishAll();
                    stopSelf();
                    break;
            }
        }
    };

    public static class MusicBinder extends Binder{

    }

    @Override
    public void onCreate() {
        super.onCreate();

        SongPlayer.addOnPlayListener(onPlayListener);

        IntentFilter filter = new IntentFilter(CONTROL_INTENT_ACTION);
        registerReceiver(broadcastReceiver,filter);

        //创建通知
        createNotification();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Song song = (Song) intent.getParcelableExtra("clickedSong");
        //播放
//        playService(SongUtil.song);

        return START_STICKY;
    }

    /**
     * 创建通知的前台服务
     */
    private void createNotification(){
        //创建通知渠道，channel
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("no_mean","前台通知服务", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null,null);  // 关闭通知声音
            notificationManager.createNotificationChannel(channel);
        }

        //设置通知的点击事件，pendingIntent
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        //创建通知的视图，remoteViews
        notificationBigLayout = new RemoteViews(getPackageName(),R.layout.notification_big);
        notificationSmallLayout = new RemoteViews(getPackageName(),R.layout.notification_small);

        notificationBigLayout.setImageViewResource(R.id.cover,R.mipmap.ic_launcher);
        notificationBigLayout.setImageViewResource(R.id.like,R.drawable.ic_heart_filled);
        notificationBigLayout.setImageViewResource(R.id.previous,R.drawable.ic_previous);
        notificationBigLayout.setImageViewResource(R.id.play,R.drawable.ic_stop);
        notificationBigLayout.setImageViewResource(R.id.next,R.drawable.ic_next);
        notificationBigLayout.setImageViewResource(R.id.cancel,R.drawable.ic_cancel);

        notificationSmallLayout.setImageViewResource(R.id.cover,R.mipmap.ic_launcher);
        notificationSmallLayout.setImageViewResource(R.id.play,R.drawable.ic_stop);
        notificationSmallLayout.setImageViewResource(R.id.next,R.drawable.ic_next);
        notificationSmallLayout.setImageViewResource(R.id.cancel,R.drawable.ic_cancel);
        // 设置点击事件
        setRemoteViewClickListener();

        //用上面的通知渠道和pendingIntent创建通知
        notificationBuilder = new NotificationCompat.Builder(this,"no_mean");
        notification = notificationBuilder
                .setSmallIcon(R.drawable.ic_logo)
                .setSound(null)
                .setCustomBigContentView(notificationBigLayout)
                .setCustomContentView(notificationSmallLayout)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        //启动前台服务
        startForeground(NOTIFICATION_ID, notification);

    }

    /**
     * 设置通知栏控件的点击监听
     */
    private void setRemoteViewClickListener(){
        Intent playIntent = new Intent(CONTROL_INTENT_ACTION);
        playIntent.putExtra(SIGNAL_KEY,PLAY_SIGNAL);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this,0,playIntent,0);

        notificationBigLayout.setOnClickPendingIntent(R.id.play,playPendingIntent);
        notificationSmallLayout.setOnClickPendingIntent(R.id.play,playPendingIntent);

        Intent nextIntent = new Intent(CONTROL_INTENT_ACTION);
        nextIntent.putExtra(SIGNAL_KEY,NEXT_SIGNAL);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this,1,nextIntent,0);
        notificationBigLayout.setOnClickPendingIntent(R.id.next,nextPendingIntent);
        notificationSmallLayout.setOnClickPendingIntent(R.id.next,nextPendingIntent);

        Intent lastIntent = new Intent(CONTROL_INTENT_ACTION);
        lastIntent.putExtra(SIGNAL_KEY,LAST_SIGNAL);
        PendingIntent lastPendingIntent = PendingIntent.getBroadcast(this,2,lastIntent,0);
        notificationBigLayout.setOnClickPendingIntent(R.id.previous,lastPendingIntent);

        Intent cancelIntent = new Intent(CONTROL_INTENT_ACTION);
        cancelIntent.putExtra(SIGNAL_KEY,CANCEL_SIGNAL);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this,3,cancelIntent,0);
        notificationBigLayout.setOnClickPendingIntent(R.id.cancel,cancelPendingIntent);
        notificationSmallLayout.setOnClickPendingIntent(R.id.cancel,cancelPendingIntent);


    }

    /**
     * 设置通知栏的内容
     * @param picUrl
     * @param name
     * @param artistName
     * @param albumName
     */
    private void setRemoteView(String picUrl,String name,String artistName,String albumName){

        Log.e(TAG,"picUrl="+picUrl+", name="+name+", artistName="+artistName+", albumName="+albumName);

        // 设置大布局
        notificationBigLayout.setTextViewText(R.id.song,name);
        notificationBigLayout.setTextViewText(R.id.artist,artistName+"-"+albumName);

        // 设置小布局
        notificationSmallLayout.setTextViewText(R.id.song,name);
        notificationSmallLayout.setTextViewText(R.id.artist,artistName+"-"+albumName);
        // 设置图片
        Glide.with(this)
                .asBitmap()
                .load(picUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        notificationBigLayout.setImageViewBitmap(R.id.cover,resource);
                        notificationSmallLayout.setImageViewBitmap(R.id.cover,resource);



                        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

    /**
     * 设置播放按钮
     * @param isPlay
     */
    private void setPlayButton(boolean isPlay){
        if(!isPlay){
            notificationBigLayout.setImageViewResource(R.id.play,R.drawable.ic_play);
            notificationSmallLayout.setImageViewResource(R.id.play,R.drawable.ic_play);
        }else{
            notificationBigLayout.setImageViewResource(R.id.play,R.drawable.ic_stop);
            notificationSmallLayout.setImageViewResource(R.id.play,R.drawable.ic_stop);
        }
        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build());
    }

    /**
     * 播放功能放在服务里
     * @param song 需要被播放的本地音乐
     */
    private void playService(Song song){
        Log.e("tag","可能不是同一个song："+song.toString());
        Log.e("tag","isPrepared: "+SongUtil.isPrepared);
        if(!SongUtil.isPrepared){
            //1. 首次播放
            SongUtil.play(Uri.parse(song.getUriStr()));
            SongUtil.isPlaying = true;
            SongUtil.isPrepared = true;
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
        return new MusicBinder();
    }

    private void cacheRecentPlay(){
        RecentPlayCache recentPlayCache = new RecentPlayCache();
        recentPlayCache.cachePlayList(SongPlayer.getPlayList());
        recentPlayCache.cacheLastSongIndex(SongPlayer.getCurSongIndex());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SongPlayer.removeOnPlayListener(onPlayListener);
        unregisterReceiver(broadcastReceiver);
        cacheRecentPlay();
    }
}
