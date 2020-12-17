package com.dss.swmusic.adapter;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dss.swmusic.R;
import com.dss.swmusic.entity.Lyric;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LyricAdapter extends BaseQuickAdapter<Lyric, BaseViewHolder> {

    public LyricAdapter(@Nullable List<Lyric> data) {
        super(R.layout.item_lyric_inline, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Lyric lyric) {
        baseViewHolder.setText(R.id.lyric,lyric.getLyric());
    }
}
