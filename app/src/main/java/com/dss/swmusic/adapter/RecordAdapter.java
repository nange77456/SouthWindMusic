package com.dss.swmusic.adapter;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dss.swmusic.R;
import com.dss.swmusic.entity.PlayerSong;
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
    List<PlayerSong> songList;
    /**
     * 唱片动画——值动画
     */
    private ValueAnimator recordAnimator = ValueAnimator.ofFloat(0,360);

    /**
     * 启动动画的项的索引
     */
    private int animationIndex = -1;

    public RecordAdapter(List<PlayerSong> songList) {
        this.songList = songList;
        // 设置旋转动画
        recordAnimator.setDuration(15000);
        recordAnimator.setRepeatMode(ValueAnimator.RESTART);
        recordAnimator.setRepeatCount(ValueAnimator.INFINITE);
        recordAnimator.setInterpolator(new LinearInterpolator());
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



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("tag","onBindViewHolder");
        PlayerSong song = songList.get(position);
        String coverImgUrl = song.getAlbums().getPicUrl();
        if(coverImgUrl!=null){
            Glide.with(holder.cover)
                    .load(coverImgUrl)
                    .into(holder.cover);
        }

        if(position == animationIndex){
            recordAnimator.removeAllUpdateListeners();
            recordAnimator.end();
            recordAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    holder.record.setRotation(value);
                    holder.cover.setRotation(value);
                }
            });
            recordAnimator.start();
        }else{
            holder.record.setRotation(0);
            holder.cover.setRotation(0);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(payloads.isEmpty()){
            onBindViewHolder(holder,position);
        }else{
            Log.e("tag","tttttttttttttttttttttttttt");
            recordAnimator.removeAllUpdateListeners();
            recordAnimator.end();
            recordAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    holder.record.setRotation(value);
                    holder.cover.setRotation(value);
                }
            });
            recordAnimator.start();
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }




    public void startRecordAnimation(int index){
        if(recordAnimator.isPaused()){
            recordAnimator.resume();
        }else{
//            startRecordAnimation(index);
            animationIndex = index;
//            notifyItemChanged(animationIndex);
            notifyItemChanged(animationIndex,"hhh");
//            notifyDataSetChanged();
        }
    }

    /**
     * 停止唱片动画
     */
    public void stopRecordAnimation(){
//        int temp = animationIndex;
        animationIndex = -1;
        recordAnimator.pause();
//        notifyItemChanged(temp);
    }



}
