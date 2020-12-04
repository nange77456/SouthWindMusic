package com.dss.swmusic.me;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.dss.swmusic.databinding.ActivityPlayBinding;

/**
 * 唱片播放页
 */
public class PlayActivity extends AppCompatActivity {
    /**
     * 布局
     */
    private ActivityPlayBinding binding;
    /**
     * 播放/暂停按钮标志
     */
    private boolean playButtonFlag = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                binding.rotatingRecord.startAnimation(playButtonFlag);
                playButtonFlag = !playButtonFlag;
            }
        });

    }
}