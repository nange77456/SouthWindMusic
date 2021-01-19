package com.dss.swmusic.adapter;


import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dss.swmusic.R;
import com.dss.swmusic.entity.LyricItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LyricAdapter extends BaseQuickAdapter<LyricItem, BaseViewHolder> {

    /**
     * 高亮的索引
     */
    private int highLightIndex = -1;

    private int highLightColor = Color.parseColor("#ffffff");

    private int normalColor = Color.parseColor("#66ffffff");

    public LyricAdapter(@Nullable List<LyricItem> data) {
        super(R.layout.item_lyric_inline, data);
    }

    public void setHighLightIndex(int index){
        this.highLightIndex = index;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, LyricItem lyric) {
        TextView textView = baseViewHolder.getView(R.id.lyric);
        textView.setText(lyric.getLyric());
        if(baseViewHolder.getAdapterPosition()-1 == highLightIndex){
            textView.setTextColor(highLightColor);
        }else{
            textView.setTextColor(normalColor);
        }
    }
}
