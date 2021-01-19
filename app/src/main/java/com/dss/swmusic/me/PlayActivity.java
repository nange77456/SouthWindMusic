package com.dss.swmusic.me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;
import com.dss.swmusic.BaseActivity;
import com.dss.swmusic.R;
import com.dss.swmusic.adapter.LyricAdapter;
import com.dss.swmusic.custom.dialog.PlayListDialog;
import com.dss.swmusic.custom.view.ProgressBar;
import com.dss.swmusic.custom.view.RotatingRecord;
import com.dss.swmusic.custom.view.TurnButton;
import com.dss.swmusic.databinding.ActivityPlayBinding;
import com.dss.swmusic.entity.PlayerSong;
import com.dss.swmusic.entity.Song;
import com.dss.swmusic.service.MusicService;
import com.dss.swmusic.util.ExtensionKt;
import com.dss.swmusic.util.LyricUtil;
import com.dss.swmusic.util.SongPlayer;
import com.dss.swmusic.util.SongUtil;
import com.dss.swmusic.util.phone.Phone1;
import com.zhouwei.blurlibrary.EasyBlur;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * 唱片播放页
 */
public class PlayActivity extends BaseActivity {
    /**
     * 布局
     */
    private ActivityPlayBinding binding;

    private final String TAG = "PlayActivity";

    private boolean scrollFlag = false;

    private DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    /**
     * 播放监听
     */
    private SongPlayer.OnPlayListener onPlayListener = new SongPlayer.SimpleOnPlayListener() {
        @Override
        public void onStart() {
            setLyric();
        }

        @Override
        public void onPlay() {
            binding.rotatingRecord.onPlay();
            binding.playButton.showStop();
        }

        @Override
        public void onPlaying(int mesc) {
            setProgressBar(mesc);
            binding.lyricView.setCurTime(mesc);
        }

        @Override
        public void onStop() {
            binding.rotatingRecord.onStop();
            binding.playButton.showPlay();
        }

        @Override
        public void onNext() {
            setToolbar();
            setBackground();

            if(scrollFlag){
                scrollFlag = false;
                return;
            }
            Log.e("tag","onNext");
            binding.rotatingRecord.onNext();
        }

        @Override
        public void onLast() {
            setToolbar();
            setBackground();

            if(scrollFlag){
                scrollFlag = false;
                return;
            }
            Log.e("tag","onLast");
            binding.rotatingRecord.onLast();
        }

        @Override
        public void onSpecialUpdateNextSong() {
            Log.e(TAG, "onSpecialUpdateNextSong: " );

            binding.rotatingRecord.updateNextSong();

            Log.e(TAG, "onSpecialUpdateNextSong: over");
        }

        @Override
        public void onSpecialUpdateLastSong() {
            Log.e(TAG, "onSpecialUpdateLastSong: " );
            binding.rotatingRecord.updateLastSong();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 设置返回按钮的点击事件
        binding.returnBtn.setOnClickListener(v -> onBackPressed());
        //设置这一页的toolbar标题
        setToolbar();
        // 设置背景图
        setBackground();
//        binding.toolbar.setTitle(SongUtil.song.getName());
//        binding.toolbar.setSubtitle(SongUtil.song.getArtist());

//        binding.lyricView.setLyrics("[00:04.050]\n[00:12.570]难以忘记初次见你\n[00:16.860]一双迷人的眼睛\n[00:21.460]在我脑海里\n[00:23.960]你的身影 挥散不去\n[00:30.160]握你的双手感觉你的温柔\n[00:34.940]真的有点透不过气\n[00:39.680]你的天真 我想珍惜\n[00:43.880]看到你受委屈 我会伤心\n[00:48.180]喔\n[00:50.340]只怕我自己会爱上你\n[00:55.070]不敢让自己靠的太近\n[00:59.550]怕我没什么能够给你\n[01:04.030]爱你也需要很大的勇气\n[01:08.190]只怕我自己会爱上你\n[01:12.910]也许有天会情不自禁\n[01:17.380]想念只让自己苦了自己\n[01:21.840]爱上你是我情非得已\n[01:28.810]难以忘记初次见你\n[01:33.170]一双迷人的眼睛\n[01:37.700]在我脑海里 你的身影 挥散不去\n[01:46.360]握你的双手感觉你的温柔\n[01:51.120]真的有点透不过气\n[01:55.910]你的天真 我想珍惜\n[02:00.150]看到你受委屈 我会伤心\n[02:04.490]喔\n[02:06.540]只怕我自己会爱上你\n[02:11.240]不敢让自己靠的太近\n[02:15.750]怕我没什么能够给你\n[02:20.200]爱你也需要很大的勇气\n[02:24.570]只怕我自己会爱上你\n[02:29.230]也许有天会情不自禁\n[02:33.680]想念只让自己苦了自己\n[02:38.140]爱上你是我情非得已\n[03:04.060]什么原因 耶\n[03:07.730]我竟然又会遇见你\n[03:13.020]我真的真的不愿意\n[03:16.630]就这样陷入爱的陷阱\n[03:20.700]喔\n[03:22.910]只怕我自己会爱上你\n[03:27.570]不敢让自己靠的太近\n[03:32.040]怕我没什么能够给你\n[03:36.560]爱你也需要很大的勇气\n[03:40.740]只怕我自己会爱上你\n[03:45.460]也许有天会情不自禁\n[03:49.990]想念只让自己苦了自己\n[03:54.510]爱上你是我情非得已\n[03:58.970]爱上你是我情非得已\n[04:03.000]\n");

        binding.rotatingRecord.init(SongPlayer.getLastSong(),
                SongPlayer.getCurSong(),
                SongPlayer.getNextSong(),
                SongPlayer.isPlaying(),
                new RotatingRecord.GetSongInterface() {
                    @Override
                    public PlayerSong getNextSong() {
                        return SongPlayer.getNextSong();
                    }

                    @Override
                    public PlayerSong getLastSong() {
                        return SongPlayer.getLastSong();
                    }
                });

        binding.rotatingRecord.setOnPageChangeListener(new RotatingRecord.OnPageChangeListener() {
            @Override
            public void onToNextPage() {
                scrollFlag = true;
                SongPlayer.playNext();
            }

            @Override
            public void onToLastPage() {
                scrollFlag = true;
                SongPlayer.playLast();
            }
        });

        if (SongPlayer.isPlaying()) {
            binding.playButton.showStop();
        } else {
            binding.playButton.showPlay();
        }


        //播放按钮点击事件
        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SongPlayer.isPlaying()) {
                    SongPlayer.pause();
                } else {
                    SongPlayer.goon();
                }

            }

        });

        // 下一首 按钮的点击事件
        binding.nextSong.setOnClickListener((v) -> {
            SongPlayer.playNext();
        });

        // 上一首按钮的点击事件
        binding.lastSong.setOnClickListener((v) -> {
            SongPlayer.playLast();
        });

        // 播放列表按钮的点击事件
        binding.playListIcon.setOnClickListener((v)->{
            PlayListDialog dialog = new PlayListDialog(this);
            dialog.show();
        });

        // 播放监听
        SongPlayer.addOnPlayListener(onPlayListener);

        // progressBar 的改变监听
        binding.progressBar.setOnRateChangeListener(rate -> {

            int curTime = (int) (SongPlayer.getDuration()*rate);
            SongPlayer.seekTo(curTime);
            setProgressBar(curTime);
        });

        // 唱片的点击事件
        binding.rotatingRecord.setOnClickListener((v)->{
            Log.e(TAG, "click 唱片");
            // 显示歌词
            binding.rotatingRecord.setVisibility(View.INVISIBLE);
            binding.lyricView.setVisibility(View.VISIBLE);
        });
        // 歌词的点击
        binding.lyricView.setOnClickListener((v)->{
            // 显示唱片
            binding.rotatingRecord.setVisibility(View.VISIBLE);
            binding.lyricView.setVisibility(View.INVISIBLE);
        });

        // 设置歌词
        setLyric();

        // 设置进度条
        setProgressBar(SongPlayer.getCurrentPosition());

        // 设置播放顺序按钮
        binding.turnButton.setTurn(SongPlayer.getPlayTurn());
        // 播放顺序的改变监听
        binding.turnButton.setOnTurnTypeChangeListener(new TurnButton.OnTurnTypeChangeListener() {
            @Override
            public void onChange(int type) {
                SongPlayer.setPlayTurn(type);
            }
        });
    }

    /**
     * 设置标题
     */
    private void setToolbar(){
        PlayerSong song = SongPlayer.getCurSong();
        binding.title.setText(song.getName());
        binding.subtitle.setText(ExtensionKt.toNiceString(song.getArtists()));
    }

    /**
     * 设置背景图
     */
    private void setBackground(){
        String url = SongPlayer.getCurSong().getAlbums().getPicUrl();
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap background = EasyBlur.with(PlayActivity.this)
                                .bitmap(resource)
                                .radius(25)
                                .scale(8)
                                .blur();
                        background = EasyBlur.with(PlayActivity.this)
                                .bitmap(background)
                                .radius(10)
                                .scale(4)
                                .blur();
                        Glide.with(binding.backgroundImg)
                                .load(background)
                                .transition(withCrossFade(factory))
                                .into(binding.backgroundImg);
                        binding.backgroundImg.setColorFilter(Color.rgb(180,180,180), PorterDuff.Mode.MULTIPLY);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * 设置进度条
     * @param curTime
     */
    private void setProgressBar(int curTime){


        binding.curTime.setText(formatTime(curTime));
        binding.endTime.setText(formatTime(SongPlayer.getDuration()));

        float rate = curTime*1f/SongPlayer.getDuration();
        binding.progressBar.setRate(rate);
    }

    /**
     * 设置歌词
     */
    private void setLyric(){
        PlayerSong song = SongPlayer.getCurSong();
        if(song.getId() != null){
            LyricUtil.getLyric(song.getId(), new LyricUtil.Callback() {
                @Override
                public void call(String lyric) {
                    if(lyric != null){
                        binding.lyricView.setLyrics(lyric);
                    }
                }
            });
        }

    }

    private String formatTime(int mesc){
        mesc = mesc/1000;
        int minute = mesc/60;
        int second = mesc%60;
        String time = "";
        if(minute < 10){
            time += "0"+minute;
        }else{
            time += minute;
        }
        time += ":";
        if(second < 10){
            time += "0"+second;
        }else{
            time += second;
        }
        return time;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.none,R.anim.slite_bottom_out);
        //把这一页的实例移到栈底，不销毁
//        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SongPlayer.removeOnPlayListener(onPlayListener);

    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("tag", "play onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("tag", "play onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("tag", "play onStop");

    }
}