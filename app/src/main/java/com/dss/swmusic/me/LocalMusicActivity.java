package com.dss.swmusic.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dss.swmusic.BaseActivity;
import com.dss.swmusic.R;
import com.dss.swmusic.databinding.ActivityLocalMusicBinding;

public class LocalMusicActivity extends BaseActivity {

    private ActivityLocalMusicBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_local_music);

        binding = ActivityLocalMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.localMusicRecycler

    }
}