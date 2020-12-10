package com.dss.swmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songList.get(position);
        String coverImgUrl = song.getCoverImageUrl();
        if(coverImgUrl!=null){

        }

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }




}
