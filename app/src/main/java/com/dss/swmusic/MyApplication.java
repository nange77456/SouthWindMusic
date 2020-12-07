package com.dss.swmusic;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class MyApplication extends Application {

    private static Context context;

    private static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler(Looper.myLooper());
    }

    public static Context getContext(){
        return context;
    }

    public static void post(Runnable r){
        handler.post(r);
    }

}
