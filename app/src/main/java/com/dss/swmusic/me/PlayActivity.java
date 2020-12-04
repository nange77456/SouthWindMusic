package com.dss.swmusic.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dss.swmusic.databinding.ActivityPlayBinding;

public class PlayActivity extends AppCompatActivity {
    ActivityPlayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}