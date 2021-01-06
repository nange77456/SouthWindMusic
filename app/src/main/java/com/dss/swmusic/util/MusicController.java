package com.dss.swmusic.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.dss.swmusic.entity.PlayerSong;
import com.dss.swmusic.service.MusicService;

public class MusicController {

    private MusicService.MusicBinder binder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MusicService.MusicBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };

    public MusicController(Context context){
        Intent intent = new Intent(context, MusicService.class);
        context.startService(intent);
        context.bindService(intent,connection,Context.BIND_AUTO_CREATE);
    }

    public void play(PlayerSong song){

    }



}
