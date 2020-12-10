package com.dss.swmusic.me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dss.swmusic.R;
import com.dss.swmusic.adapter.LocalSongAdapter;
import com.dss.swmusic.databinding.ActivityLocalMusicBinding;
import com.dss.swmusic.entity.Song;
import com.dss.swmusic.util.phone.Phone1;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicActivity extends AppCompatActivity {
    /**
     * 布局
     */
    private ActivityLocalMusicBinding binding;
    /**
     * 歌曲列表
     */
    private List<Song> songList = new ArrayList<>();

    private LocalSongAdapter adapter = new LocalSongAdapter(songList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用ViewBinding来加载布局
        binding = ActivityLocalMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //设置toolbar，和menu搭配使用
        setSupportActionBar(binding.toolbar);

        //设置本地音乐RecyclerView
        binding.localMusicRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.localMusicRecycler.setAdapter(adapter);

        //扫描本地音乐，初始化songList
        startScanLocalMusic();
        adapter.notifyDataSetChanged();

        adapter.setSongPositionPhone(new Phone1<Integer>() {
            @Override
            public void onPhone(Integer position) {
                Song song = songList.get(position);
                Intent intent = new Intent(LocalMusicActivity.this,PlayActivity.class);
                intent.putExtra("clickedSong",song);
                Log.e("tag","最初的adpter里点击的song："+song.getName());
                startActivity(intent);
            }
        });

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载menu布局文件
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //TODO
            case R.id.manage:break;
            case R.id.scan:
                startScanLocalMusic();
                //TODO songList更新了

                break;
            case R.id.sort:break;
            case R.id.search:break;
        }
        return true;
    }

    /**
     * 调用scanLocalMusic方法，如果没有权限就申请
     */
    public void startScanLocalMusic(){
        //每次扫描清空内存
        songList.clear();
        //判断是否已经有访问本地外部存储空间的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(
                        this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            //扫描本地音乐
            scanLocalMusic();
        }
    }

    /**
     * 扫描本地音乐
     */
    //TODO 卸载安装第一次读不到本地音乐
    public void scanLocalMusic(){
        //获取内容提供器
        ContentResolver resolver = getContentResolver();
        //访问音频文件数据库，获取游标，筛选‘audio/mpeg’类型的音频
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null
                ,MediaStore.Audio.Media.MIME_TYPE+"='audio/mpeg'"
//                ,null
                ,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        while (cursor.moveToNext()){
            //读取构造一首Song需要的数据
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String parent = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME));
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            //把id转换为uri
            String uriStr = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI+"/"+id;

            //构造Song
            Song curr = new Song(name,path,album,artist,size,duration,parent,uriStr);

            /*//好像没有用
            if (size > 1000 * 800) {
                // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                if (name.contains("-")) {
                    String[] str = name.split("-");
                    curr.setArtist(str[0].trim());
                    curr.setName(str[1].trim());
                }
            }*/
            //添加到songList结尾
            songList.add(curr);
        }

        cursor.close();

    }
}