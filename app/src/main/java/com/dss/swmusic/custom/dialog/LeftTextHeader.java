package com.dss.swmusic.custom.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dss.swmusic.R;

import org.jetbrains.annotations.NotNull;

import fun.inaction.dialog.ViewAdapter;

public class LeftTextHeader implements ViewAdapter {

    private ViewGroup parent;
    private View view;
    private TextView textView;

    public LeftTextHeader(ViewGroup parent) {
        this.parent = parent;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_header,parent,false);
        textView = view.findViewById(R.id.title);
    }

    @NotNull
    @Override
    public View getView() {
        return view;
    }

    public void setTitle(String title){
        textView.setText(title);
    }
}
