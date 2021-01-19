package com.dss.swmusic.adapter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dss.swmusic.R;
import com.dss.swmusic.entity.PlayerSong;
import com.dss.swmusic.util.ExtensionKt;

import org.jetbrains.annotations.NotNull;

public class PlayListDialogAdapter extends BaseQuickAdapter<PlayerSong, BaseViewHolder> {

    private int selectedItemIndex = -1;

    public PlayListDialogAdapter() {
        super(R.layout.item_playlist_dialog);
    }

    public void setSelectedItem(int index){
        int temp = selectedItemIndex;
        selectedItemIndex = index;
        if(temp != -1){
            notifyItemChanged(temp);
        }
        notifyItemChanged(selectedItemIndex);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PlayerSong song) {
        baseViewHolder.setText(R.id.songName,song.getName());
        baseViewHolder.setText(R.id.artistName," - "+ ExtensionKt.toNiceString(song.getArtists()));
        ImageView volumeIcon = baseViewHolder.getView(R.id.volumn_icon);
        if(baseViewHolder.getAdapterPosition() == selectedItemIndex){
            volumeIcon.setVisibility(View.VISIBLE);
            baseViewHolder.setTextColorRes(R.id.songName,R.color.colorTheme);
            baseViewHolder.setTextColorRes(R.id.artistName,R.color.colorTheme);
        }else{
            volumeIcon.setVisibility(View.GONE);
            baseViewHolder.setTextColorRes(R.id.songName,R.color.colorText);
            baseViewHolder.setTextColorRes(R.id.artistName,R.color.colorTextLight);
        }
    }
}
