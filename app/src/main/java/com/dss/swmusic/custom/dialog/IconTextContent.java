package com.dss.swmusic.custom.dialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dss.swmusic.R;
import com.dss.swmusic.adapter.IconTextContentAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import fun.inaction.dialog.ViewAdapter;
import kotlin.Pair;

public class IconTextContent implements ViewAdapter {

    private ViewGroup parent;
    private View view;
    private RecyclerView recyclerView;
    private IconTextContentAdapter adapter = new IconTextContentAdapter();

    private OnItemClickListener listener;

    public IconTextContent(ViewGroup parent) {
        this.parent = parent;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_view_list_content_with_icon,parent,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Log.e("tag","click item");
                if(listener != null){
                    listener.onItemClick(adapter,view,position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
    }

    @NotNull
    @Override
    public View getView() {
        return view;
    }

    public void setData(List<Pair<Integer,String>> data){
        adapter.setNewInstance(data);
    }

    public void setOnItemClickListener(OnItemClickListener l){
        this.listener = l;
    }

}
