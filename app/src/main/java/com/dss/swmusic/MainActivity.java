package com.dss.swmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 跳转到主页面后，销毁其他页面
        ActivityCollector.INSTANCE.finishOthers(this);

    }
}