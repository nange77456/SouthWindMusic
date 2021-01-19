package com.dss.swmusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dss.swmusic.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kotlin.Pair;

public class IconTextContentAdapter extends BaseQuickAdapter<Pair<Integer,String>,BaseViewHolder> {


    public IconTextContentAdapter() {
        super(R.layout.item_song_options);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Pair<Integer, String> integerStringPair) {
        baseViewHolder.setImageResource(R.id.iconImageView,integerStringPair.getFirst());
        baseViewHolder.setText(R.id.text,integerStringPair.getSecond());
    }
}
