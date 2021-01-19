package com.dss.swmusic.custom.dialog;

import android.content.Context;
import android.graphics.Point;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.dss.swmusic.entity.PlayerSong;
import com.dss.swmusic.util.SongPlayer;

import org.jetbrains.annotations.NotNull;

import fun.inaction.dialog.BottomDialog;

public class PlayListDialog extends BottomDialog {

    private PlayListDialogContent content = new PlayListDialogContent(getContentContainer());

    public PlayListDialog(@NotNull Context context) {
        super(context);
        setContent(content);
        //获取屏幕大小
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point screenSize = new Point();
        wm.getDefaultDisplay().getSize(screenSize);
        // 设置宽高
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(screenSize.x, screenSize.y/2);
        setLayoutParams(lp);

        // 设置数据
        content.setSize(SongPlayer.getPlayList().size());
        content.setListData(SongPlayer.getPlayList());
        content.setSelectedItem(SongPlayer.getCurSongIndex());
        content.setOnItemClickListener(new PlayListDialogContent.OnClickListener() {
            @Override
            public void onClick(int position) {
                PlayerSong song = SongPlayer.getPlayList().get(position);
                SongPlayer.play(song);
            }
        });
    }
}
