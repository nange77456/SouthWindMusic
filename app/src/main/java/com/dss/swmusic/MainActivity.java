package com.dss.swmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dss.swmusic.custom.view.MainTabLayout;
import com.dss.swmusic.databinding.ActivityMainBinding;
import com.dss.swmusic.discover.DiscoverFragment;
import com.dss.swmusic.me.MeFragment;
import com.dss.swmusic.me.PlayActivity;
import com.dss.swmusic.me.SearchActivity;
import com.dss.swmusic.service.MusicService;
import com.dss.swmusic.video.VideoFragment;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        // 禁止滑动退出
//        setSwipeBack(false);

        // 跳转到主页面后，销毁其他页面
        ActivityCollector.INSTANCE.finishOthers(this);

        fragmentList.add(new MeFragment());
        fragmentList.add(new DiscoverFragment());
        fragmentList.add(new VideoFragment());

        // 设置ViewPager
        binding.viewPager.setOffscreenPageLimit(2);
        binding.viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager(),FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });

        binding.mainTabLayout.setViewPager(binding.viewPager);
        binding.mainTabLayout.setOnClickCurItemListener(new MainTabLayout.OnClickCurItemListener() {
            @Override
            public void clickCurItem(int index) {
                switch (index){
                    case 0:
                        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        // 在视频页点击视频标签时，滑动到顶部
                        ((VideoFragment)fragmentList.get(2)).scrollToTop();
                        break;
                }
            }
        });

        // 启动服务
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);

    }

    /**
     * 搜索按钮的点击事件
     * @param v
     */
    public void onClickSearchIcon(View v){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

}