package com.dss.swmusic.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.dss.swmusic.BaseActivity;
import com.dss.swmusic.R;
import com.dss.swmusic.databinding.ActivityEntryBinding;

public class EntryActivity extends BaseActivity {

    private ActivityEntryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEntryBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        // 点击“手机号登录“按钮
        binding.loginButton.setOnClickListener(v->{
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        });

    }
}