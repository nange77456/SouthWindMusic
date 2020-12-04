package com.dss.swmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;

import com.dss.swmusic.databinding.ActivityMainBinding;
import com.dss.swmusic.discover.DiscoverFragment;
import com.dss.swmusic.me.MeFragment;
import com.dss.swmusic.video.VideoFragment;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        // 跳转到主页面后，销毁其他页面
        ActivityCollector.INSTANCE.finishOthers(this);

        binding.viewPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:return new MeFragment();
                    case 1:return new DiscoverFragment();
                    case 2:return new VideoFragment();
                }
                return null;
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });
        binding.mainTabLayout.setViewPager2(binding.viewPager);

    }
}