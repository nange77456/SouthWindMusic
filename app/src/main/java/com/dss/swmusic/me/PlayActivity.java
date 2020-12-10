package com.dss.swmusic.me;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dss.swmusic.databinding.ActivityPlayBinding;
import com.dss.swmusic.entity.Song;
import com.dss.swmusic.service.MusicService;
import com.dss.swmusic.util.SongUtil;

/**
 * 唱片播放页
 */
public class PlayActivity extends AppCompatActivity {
    /**
     * 布局
     */
    private ActivityPlayBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        Log.e("tag","play onCreate");

    }


    @Override
    public void onBackPressed() {
        //把这一页的实例移到栈底，不销毁
        moveTaskToBack(true);
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

        Log.e("tag","play onDestroy");

    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Song song = (Song) intent.getSerializableExtra("clickedSong");
        Log.e("tag","song in onStart is "+song.getName());

        //播放按钮点击事件
        binding.button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //启动前台服务
                Intent notificationIntent = new Intent(PlayActivity.this, MusicService.class);
                notificationIntent.putExtra("clickedSong",song);
                startService(notificationIntent);
                //启动动画
                binding.rotatingRecord.startAnimation(SongUtil.isPlaying);
            }


        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("tag","play onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("tag","play onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("tag","play onStop");

    }
}