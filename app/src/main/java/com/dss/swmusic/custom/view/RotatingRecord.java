package com.dss.swmusic.custom.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.viewpager2.widget.ViewPager2;

import com.dss.swmusic.R;
import com.dss.swmusic.adapter.RecordAdapter;
import com.dss.swmusic.entity.Song;
import com.dss.swmusic.util.phone.Phone;
import com.dss.swmusic.util.phone.Phone1;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 旋转的唱片
 * 自定义view，组合控件
 */
public class RotatingRecord extends FrameLayout {
    /**
     * 针压动画——视图动画
     */
    private RotateAnimation pinAnimation1;
    /**
     * 针压动画——视图动画
     */
    private RotateAnimation pinAnimation2;
    /**
     * 针压图片视图
     */
    private ImageView pin;
    /**
     * 唱片视图
     */
    private ViewPager2 recordPager;
    /**
     * 歌曲列表，给图片数据
     * !!要new，不然adapter第一次拿到null会报错
     */
    private List<Song> songList = new ArrayList<>();
    /**
     * 唱片viewPager2的adapter
     */
    private RecordAdapter adapter = new RecordAdapter(songList);
    /**
     * 通知外部类recordPager滑动到第几页
     */
    public Phone1<Integer> currentSongPhone;


    /**
     * 使用RotatingRecord必须初始化歌曲列表
     * @param songList
     */
    public void setSongList(List<Song> songList) {
        this.songList.addAll(songList);
        adapter.notifyDataSetChanged();
    }

    public RotatingRecord(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_view_rotating_record,this);
        pin = view.findViewById(R.id.pin);
        recordPager = view.findViewById(R.id.recordPager);
        //唱片视图设置adapter
        recordPager.setAdapter(adapter);
        //设置滑动唱片切换正在播放的歌曲
        recordPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //通知这个自定义view的使用者 滑动到第position页了
                if(currentSongPhone!=null){
                    currentSongPhone.onPhone(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                switch (state){
                    case ViewPager2.SCROLL_STATE_DRAGGING:
                        //TODO 歌曲继续播放，针压采用恢复动画
                        break;
                    case ViewPager2.SCROLL_STATE_IDLE:

                        break;
                    case ViewPager2.SCROLL_STATE_SETTLING:

                        break;
                }

            }
        });
        //设置点击唱片切换到歌词页
        recordPager.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //让针压起始装态是远离唱片的
        pin.setPivotX(dp2px(context,18));
        pin.setPivotY(dp2px(context,16));
        pin.setRotation(-20);

        //设置针压动画1
        pinAnimation1 = new RotateAnimation(0,20,dp2px(context,18),dp2px(context,16));
        pinAnimation1.setDuration(500);
        pinAnimation1.setFillAfter(true);
        //设置针压动画2
        pinAnimation2 = new RotateAnimation(20,0,dp2px(context,18),dp2px(context,16));
        pinAnimation2.setDuration(500);
        pinAnimation2.setFillAfter(true);


    }

    /**
     * 让viewPager划到指定页
     * @param index
     */
    public void setCurrentPage(int index){
        recordPager.setCurrentItem(index,false);
    }

    /**
     * 启动唱片和针压的动画
     * @param flag
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAnimation(boolean flag){
        if(flag){
            pin.startAnimation(pinAnimation1);
        }else{
            pin.startAnimation(pinAnimation2);
        }
        adapter.startRecordAnimation(flag);

    }

    /**
     * px转dp静态方法
     * @param context
     * @param pxValue
     * @return
     */
    private static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dp转px静态方法
     * @param context
     * @param dpValue
     * @return
     */
    private static int dp2px(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }


}
