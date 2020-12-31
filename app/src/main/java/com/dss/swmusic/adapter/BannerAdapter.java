package com.dss.swmusic.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.dss.swmusic.R;
import com.dss.swmusic.network.bean.Banner;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

/**
 * BannerViewPager 的adapter
 */
public class BannerAdapter extends BaseBannerAdapter<Banner,BannerAdapter.ViewHolder> {


    public static class ViewHolder extends BaseViewHolder<Banner>{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Banner data, int position, int pageSize) {
            ImageView imageView = findView(R.id.banner_image);
            Glide.with(imageView)
                    .load(data.getPic())
                    .into(imageView);
        }
    }

    @Override
    protected void onBind(ViewHolder holder, Banner data, int position, int pageSize) {
        holder.bindData(data,position,pageSize);
    }

    @Override
    public ViewHolder createViewHolder(@NonNull ViewGroup parent, View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_banner;
    }


}
