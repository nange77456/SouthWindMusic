package com.dss.swmusic.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dss.swmusic.R;

import org.jetbrains.annotations.NotNull;

import kotlin.Pair;

public class ListContentAdapter extends BaseQuickAdapter<Pair<String,String>, BaseViewHolder> {

    public ListContentAdapter() {
        super(R.layout.item_list_content);
    }



    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Pair<String, String> stringStringPair) {
        ImageView imageView = baseViewHolder.getView(R.id.icon);
        Glide.with(imageView)
                .load(stringStringPair.getFirst())
                .into(imageView);
        baseViewHolder.setText(R.id.textView,stringStringPair.getSecond());
    }
}
