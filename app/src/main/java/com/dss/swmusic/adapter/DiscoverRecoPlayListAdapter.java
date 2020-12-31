package com.dss.swmusic.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dss.swmusic.R;
import com.dss.swmusic.network.bean.RecommendPlayList;

import org.jetbrains.annotations.NotNull;

public class DiscoverRecoPlayListAdapter extends BaseQuickAdapter<RecommendPlayList, BaseViewHolder> {

    public DiscoverRecoPlayListAdapter() {
        super(R.layout.item_discover_recommend_playlist);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, RecommendPlayList recommendPlayList) {
        ImageView imageView = baseViewHolder.getView(R.id.playListImg);
        Glide.with(imageView)
                .load(recommendPlayList.getPicUrl())
                .into(imageView);
        baseViewHolder.setText(R.id.playListName,recommendPlayList.getName());
    }
}
