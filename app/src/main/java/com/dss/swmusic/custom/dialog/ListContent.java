package com.dss.swmusic.custom.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dss.swmusic.R;
import com.dss.swmusic.adapter.ListContentAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import fun.inaction.dialog.ViewAdapter;
import kotlin.Pair;

public class ListContent implements ViewAdapter {

    private ViewGroup parent;
    private View view;
    private RecyclerView recyclerView;
    private ListContentAdapter adapter = new ListContentAdapter();

    public ListContent(ViewGroup parent) {
        this.parent = parent;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_view_list_content_with_icon,parent,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
    }

    @NotNull
    @Override
    public View getView() {
        return view;
    }

    public void setData(List<Pair<String,String>> data){
        adapter.setNewInstance(data);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        adapter.setOnItemClickListener(listener);
    }

}
