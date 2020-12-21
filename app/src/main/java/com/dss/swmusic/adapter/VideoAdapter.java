package com.dss.swmusic.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dss.swmusic.R;
import com.dss.swmusic.custom.view.MyVideoPlayer;
import com.dss.swmusic.custom.view.SampleCoverVideo;
import com.dss.swmusic.network.bean.VideoData;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.jetbrains.annotations.NotNull;

public class VideoAdapter extends BaseQuickAdapter<VideoData, BaseViewHolder> {


    public VideoAdapter() {
        super(R.layout.item_video);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, VideoData videoData) {
//        Log.e("tag","video data ="+videoData);
        // 设置标题
        baseViewHolder.setText(R.id.videoTitle,videoData.getData().getTitle());
        // 设置视频播放器
        SampleCoverVideo videoPlayer = baseViewHolder.getView(R.id.videoView);
        videoPlayer.setUpLazy(videoData.getData().getUrlInfo().getUrl()
                ,true,null,null,"这是title");
        // 防止错位
        videoPlayer.setPlayPosition(baseViewHolder.getAdapterPosition());
        // 隐藏标题和返回按钮
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        videoPlayer.getBackButton().setVisibility(View.GONE);
        // 关闭流量播放提示
        videoPlayer.setNeedShowWifiTip(false);
        // 全屏时横屏
        videoPlayer.setRotateViewAuto(true);
        videoPlayer.setLockLand(true);
        // 设置封面
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(imageView)
                .load(videoData.getData().getCoverUrl())
                .into(imageView);
        videoPlayer.getThumbImageViewLayout().setVisibility(View.VISIBLE);
//        Log.e("tag","coverUrl = "+videoData.getData().getCoverUrl());
        videoPlayer.setThumbImageView(imageView);
        // 设置全屏按钮
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.startWindowFullscreen(getContext(),false,true);
            }
        });
        // 设置全屏标题
        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack(){
            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                videoPlayer.getCurrentPlayer().getTitleTextView().setText(videoData.getData().getTitle());
            }

        });
    }
}
