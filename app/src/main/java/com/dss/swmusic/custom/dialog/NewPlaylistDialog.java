package com.dss.swmusic.custom.dialog;

import android.content.Context;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import fun.inaction.dialog.CenterDialog;

public class NewPlaylistDialog extends CenterDialog {

    LeftTextHeader header = new LeftTextHeader(getHeaderContainer());
    EditView content = new EditView(getContentContainer());

    public NewPlaylistDialog(@NotNull Context context) {
        super(context);

        setHeader(header);
        setContent(content);

        header.setTitle("新建歌单");
        content.editTextView.setHint("请输入歌单标题");;

    }

    public NewPlaylistDialog setOnCancelBtnClickListener(View.OnClickListener l){
        content.cancelButton.setOnClickListener(l);
        return this;
    }

    public NewPlaylistDialog setOnConfirmBtnClickListener(View.OnClickListener l){
        content.confirmButton.setOnClickListener(l);
        return this;
    }

    public String getEditText(){
        return content.editTextView.getText();
    }




}
