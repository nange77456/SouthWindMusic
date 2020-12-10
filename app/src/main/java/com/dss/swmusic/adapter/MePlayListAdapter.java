package com.dss.swmusic.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dss.swmusic.R;
import com.dss.swmusic.network.bean.PlayList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * “我的” 页显示的歌单（创建歌单，收藏歌单）的RecyclerViewAdapter
 */
public class MePlayListAdapter extends BaseQuickAdapter<PlayList, BaseViewHolder> {

    public MePlayListAdapter(List<PlayList> data) {
        super(R.layout.item_me_paly_list,data);
    }

    public MePlayListAdapter(){
        super(R.layout.item_me_paly_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PlayList playList) {
        // 设置封面图片
        ImageView imageView = baseViewHolder.getView(R.id.convertImageView);
        Glide.with(imageView)
                .load(playList.getCoverImgUrl())
//                .load(R.drawable.profile)
                .into(imageView );
        // 设置歌单名
        baseViewHolder.setText(R.id.playListName,playList.getName());
        // 设置歌单里歌曲数量信息
        baseViewHolder.setText(R.id.playListNumText,playList.getTrackCount()+"首");
    }
}
