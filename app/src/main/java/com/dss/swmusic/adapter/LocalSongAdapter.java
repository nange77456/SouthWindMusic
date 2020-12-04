package com.dss.swmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dss.swmusic.R;
import com.dss.swmusic.entity.LocalSong;
import com.dss.swmusic.util.phone.Phone1;

import java.util.List;

/**
 * 本地音乐adapter
 */
public class LocalSongAdapter extends RecyclerView.Adapter<LocalSongAdapter.ViewHolder> {
    /**
     * 本地音乐列表，由构造函数传入
     */
    private List<LocalSong> songList;
    /**
     * 歌曲位置值回调接口
     */
    private Phone1<Integer> songPositionPhone;

    public void setSongPositionPhone(Phone1<Integer> songPositionPhone) {
        this.songPositionPhone = songPositionPhone;
    }

    public LocalSongAdapter(List<LocalSong> songList) {
        this.songList = songList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameText;
        TextView artistAndAlbumText;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            nameText = itemView.findViewById(R.id.nameText);
            artistAndAlbumText = itemView.findViewById(R.id.artistAndAlbumText);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_local_music,parent,false);
        ViewHolder holder = new ViewHolder(view);
        //歌曲的点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songPositionPhone.onPhone(holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalSong curr = songList.get(position);
        holder.nameText.setText(curr.getName());
        holder.artistAndAlbumText.setText(curr.getArtist()+" - "+curr.getAlbum());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }



}
