package com.dss.swmusic.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dss.swmusic.R;
import com.dss.swmusic.entity.MeFunctionItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class DiscoverBtnGroupAdapter extends BaseQuickAdapter<MeFunctionItem, BaseViewHolder> {


    public DiscoverBtnGroupAdapter( @Nullable List<MeFunctionItem> data) {
        super(R.layout.item_discover_function_group, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MeFunctionItem meFunctionItem) {
        ImageView iconImg = baseViewHolder.getView(R.id.iconImageView);
        Glide.with(iconImg)
                .load(meFunctionItem.getImageId())
                .into(iconImg);
        baseViewHolder.setText(R.id.buttonName,meFunctionItem.getText());
    }
}
