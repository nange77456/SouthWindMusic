package com.dss.swmusic.custom.dialog;

import android.content.Context;

import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import fun.inaction.dialog.BottomDialog;
import fun.inaction.dialog.content.IconTextListContent;
import kotlin.Pair;

public class BottomSheetDialog extends BottomDialog {

    private DialogSongHeader header = new DialogSongHeader(getHeaderContainer());
    private IconTextContent content = new IconTextContent(getContentContainer());


    public BottomSheetDialog(@NotNull Context context) {
        super(context);

        setHeader(header);
        setContent(content);
    }

    public BottomSheetDialog setHeader(String url,String songName,String artistName){
        header.setAlbumPic(url);
        header.setSongName(songName);
        header.setArtistName(artistName);
        return this;
    }

    public BottomSheetDialog setListData(List<Pair<Integer,String>> data){
        content.setData(data);
        return this;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        content.setOnItemClickListener(listener);
    }

}
