package com.dss.swmusic.adapter;

import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dss.swmusic.R;
import com.dss.swmusic.entity.Song;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 旋转的唱片自定义view里的viewPager2的adapter
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    /**
     * 本地音乐列表
     */
    List<Song> songList;
    /**
     * 唱片动画——值动画
     */
    private ValueAnimator recordAnimator = ValueAnimator.ofFloat(0,360);

    public RecordAdapter(List<Song> songList) {
        this.songList = songList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView record;
        CircleImageView cover;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            record = itemView.findViewById(R.id.record);
            cover = itemView.findViewById(R.id.cover);
            view = itemView;


        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record,parent,false);
        ViewHolder holder = new ViewHolder(view);

        //设置唱片的动画
//        recordAnimator = ;
        recordAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                holder.record.setRotation(value);
                holder.cover.setRotation(value);
            }
        });
        recordAnimator.setDuration(15000);
        recordAnimator.setRepeatMode(ValueAnimator.RESTART);
        recordAnimator.setRepeatCount(ValueAnimator.INFINITE);
        recordAnimator.setInterpolator(new LinearInterpolator());

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songList.get(position);
        String coverImgUrl = song.getCoverImageUrl();
        if(coverImgUrl!=null){
            Glide.with(holder.cover)
                    .load(coverImgUrl)
                    .into(holder.cover);
        }



    }

    @Override
    public int getItemCount() {
        return songList.size();
    }


    /**
     * 启动唱片的动画（旋转）
     * @param flag
     */
    public void startRecordAnimation(boolean flag){
        if(flag){
            if(!recordAnimator.isStarted()){
                recordAnimator.start();
            }else{
                recordAnimator.resume();
            }
        }else{
            recordAnimator.pause();
        }
    }



}
