package com.dss.swmusic.me;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dss.swmusic.BaseActivity;
import com.dss.swmusic.adapter.LyricAdapter;
import com.dss.swmusic.custom.view.RotatingRecord;
import com.dss.swmusic.databinding.ActivityPlayBinding;
import com.dss.swmusic.entity.PlayerSong;
import com.dss.swmusic.entity.Song;
import com.dss.swmusic.service.MusicService;
import com.dss.swmusic.util.ExtensionKt;
import com.dss.swmusic.util.SongPlayer;
import com.dss.swmusic.util.SongUtil;
import com.dss.swmusic.util.phone.Phone1;

import java.util.ArrayList;

/**
 * 唱片播放页
 */
public class PlayActivity extends BaseActivity {
    /**
     * 布局
     */
    private ActivityPlayBinding binding;

    /**
     * 播放监听
     */
    private SongPlayer.OnPlayListener onPlayListener = new SongPlayer.SimpleOnPlayListener() {
        @Override
        public void onStart() {
        }

        @Override
        public void onPlay() {
            binding.rotatingRecord.onPlay();
            binding.playButton.showStop();
        }

        @Override
        public void onStop() {
            binding.rotatingRecord.onStop();
            binding.playButton.showPlay();
        }

        @Override
        public void onNext() {
//            Log.e("tag","onNext");
            binding.rotatingRecord.onNext();
//            binding.rotatingRecord.addNext(SongPlayer.getNextSong());
        }

        @Override
        public void onLast() {
//            Log.e("tag","onLast");
            binding.rotatingRecord.onLast();
//            binding.rotatingRecord.addLast(SongPlayer.getLastSong());
        }

    };
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //设置这一页的toolbar标题
//        binding.toolbar.setTitle(SongUtil.song.getName());
//        binding.toolbar.setSubtitle(SongUtil.song.getArtist());

//        binding.lyricView.setLyrics("[00:04.050]\n[00:12.570]难以忘记初次见你\n[00:16.860]一双迷人的眼睛\n[00:21.460]在我脑海里\n[00:23.960]你的身影 挥散不去\n[00:30.160]握你的双手感觉你的温柔\n[00:34.940]真的有点透不过气\n[00:39.680]你的天真 我想珍惜\n[00:43.880]看到你受委屈 我会伤心\n[00:48.180]喔\n[00:50.340]只怕我自己会爱上你\n[00:55.070]不敢让自己靠的太近\n[00:59.550]怕我没什么能够给你\n[01:04.030]爱你也需要很大的勇气\n[01:08.190]只怕我自己会爱上你\n[01:12.910]也许有天会情不自禁\n[01:17.380]想念只让自己苦了自己\n[01:21.840]爱上你是我情非得已\n[01:28.810]难以忘记初次见你\n[01:33.170]一双迷人的眼睛\n[01:37.700]在我脑海里 你的身影 挥散不去\n[01:46.360]握你的双手感觉你的温柔\n[01:51.120]真的有点透不过气\n[01:55.910]你的天真 我想珍惜\n[02:00.150]看到你受委屈 我会伤心\n[02:04.490]喔\n[02:06.540]只怕我自己会爱上你\n[02:11.240]不敢让自己靠的太近\n[02:15.750]怕我没什么能够给你\n[02:20.200]爱你也需要很大的勇气\n[02:24.570]只怕我自己会爱上你\n[02:29.230]也许有天会情不自禁\n[02:33.680]想念只让自己苦了自己\n[02:38.140]爱上你是我情非得已\n[03:04.060]什么原因 耶\n[03:07.730]我竟然又会遇见你\n[03:13.020]我真的真的不愿意\n[03:16.630]就这样陷入爱的陷阱\n[03:20.700]喔\n[03:22.910]只怕我自己会爱上你\n[03:27.570]不敢让自己靠的太近\n[03:32.040]怕我没什么能够给你\n[03:36.560]爱你也需要很大的勇气\n[03:40.740]只怕我自己会爱上你\n[03:45.460]也许有天会情不自禁\n[03:49.990]想念只让自己苦了自己\n[03:54.510]爱上你是我情非得已\n[03:58.970]爱上你是我情非得已\n[04:03.000]\n");
        binding.toolbar.setTitle(SongPlayer.getCurSong().getName());
        binding.toolbar.setSubtitle(ExtensionKt.toNiceString(SongPlayer.getCurSong().getArtists()));

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
                SongPlayer.playNext();
            }

            @Override
            public void onToLastPage() {
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (SongPlayer.isPlaying()) {
                    SongPlayer.pause();
                } else {
                    SongPlayer.goon();
                }

            }

        });

        binding.nextSong.setOnClickListener((v) -> {
            Log.e("tag", "click next song");
            SongPlayer.playNext();
        });

        binding.lastSong.setOnClickListener((v) -> {
            SongPlayer.playLast();
        });

        // 播放监听
        SongPlayer.addOnPlayListener(onPlayListener);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //把这一页的实例移到栈底，不销毁
//        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*//停止播放
        if(SongUtil.player.isPlaying()){
            SongUtil.player.stop();
        }
        //释放资源
        SongUtil.player.release();*/

        SongPlayer.removeOnPlayListener(onPlayListener);
        Log.e("tag", "play onDestroy");

    }


    @Override
    protected void onStart() {
        super.onStart();
//        binding.rotatingRecord.setSongList(SongUtil.songList);
//        binding.rotatingRecord.setCurrentPage(SongUtil.songList.indexOf(SongUtil.song));
        //标题
//        binding.toolbar.setTitle(SongUtil.song.getName());
//        binding.toolbar.setSubtitle(SongUtil.song.getArtist());


        //设置唱片滑动事件
//        binding.rotatingRecord.currentSongPhone = new Phone1<Integer>() {
//            @Override
//            public void onPhone(Integer position) {
//
//                SongUtil.song = SongUtil.songList.get(position);
//                SongUtil.isPrepared = false;
//
//                //标题
//                binding.toolbar.setTitle(SongUtil.song.getName());
//                binding.toolbar.setSubtitle(SongUtil.song.getArtist());
//            }
//        };

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