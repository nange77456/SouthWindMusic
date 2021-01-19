package com.dss.swmusic.custom.dialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dss.swmusic.R;
import com.dss.swmusic.adapter.PlayListDialogAdapter;
import com.dss.swmusic.entity.PlayerSong;
import com.dss.swmusic.util.SongPlayer;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import fun.inaction.dialog.ViewAdapter;

public class PlayListDialogContent implements ViewAdapter {

    private ViewGroup parent;
    private View view;
    private RecyclerView recyclerView;
    private TextView sizeTextView;
    private PlayListDialogAdapter adapter = new PlayListDialogAdapter();
    private OnClickListener onClickListener;

    public PlayListDialogContent(ViewGroup parent) {
        this.parent = parent;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_content,parent,false);
        sizeTextView = view.findViewById(R.id.playListSizeText);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        adapter.setOnItemClickListener((adapter, view, position) -> {
            ((PlayListDialogAdapter)adapter).setSelectedItem(position);
            if(onClickListener != null){
                onClickListener.onClick(position);
            }
        });
    }

    @NotNull
    @Override
    public View getView() {
        return view;
    }

    public void setSize(int size){
        sizeTextView.setText("("+size+")");
    }

    public void setListData(List<PlayerSong> songs){
        adapter.setNewInstance(songs);
    }

    public void setSelectedItem(int index){
        adapter.setSelectedItem(index);
        recyclerView.scrollToPosition(index);
    }

    public void setOnItemClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener{
        void onClick(int position);
    }

}
