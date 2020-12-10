package com.dss.swmusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dss.swmusic.R;
import com.dss.swmusic.network.bean.Song;

import org.jetbrains.annotations.NotNull;

public class PlayListAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    public PlayListAdapter() {
        super(R.layout.item_play_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Song song) {
        baseViewHolder.setText(R.id.index,baseViewHolder.getAdapterPosition()+1+"");
        baseViewHolder.setText(R.id.songName,song.getName());
        String authors = "";
        for(int i=0;i<song.getAr().size()-1;++i){
            authors = authors+song.getAr().get(i).getName()+"/";
        }
        authors += song.getAr().get(song.getAr().size()-1).getName();
        baseViewHolder.setText(R.id.songFrom,authors+" - "+song.getAl().getName());
    }
}
