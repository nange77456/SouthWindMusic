package com.dss.swmusic.adapter;

import android.graphics.Typeface;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dss.swmusic.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HotSearchAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public HotSearchAdapter() {
        super(R.layout.item_hot_search_key);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String key) {
        int index = baseViewHolder.getAdapterPosition();
        baseViewHolder.setText(R.id.indexTextView,index+1+"");
        baseViewHolder.setText(R.id.keyTextView,key);
        if(index >= 0 && index <= 2){
            baseViewHolder.setTextColor(R.id.indexTextView, ContextCompat.getColor(getContext(),R.color.colorTheme));
            TextView keyTextView = baseViewHolder.getView(R.id.keyTextView);
            keyTextView.setTypeface(keyTextView.getTypeface(), Typeface.BOLD);
        }
    }
}
