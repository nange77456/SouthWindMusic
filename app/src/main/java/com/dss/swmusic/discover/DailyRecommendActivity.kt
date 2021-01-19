package com.dss.swmusic.discover

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dss.swmusic.BaseActivity
import com.dss.swmusic.R
import com.dss.swmusic.adapter.PlayListAdapter
import com.dss.swmusic.adapter.diff.SongDiffCallback
import com.dss.swmusic.databinding.ActivityPlayListDetailBinding
import com.dss.swmusic.discover.viewmodel.DailyRecommendViewModel
import com.dss.swmusic.network.bean.Song
import com.dss.swmusic.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhouwei.blurlibrary.EasyBlur
import kotlinx.android.synthetic.main.activity_play_list_detail.*

/**
 * 每日推荐歌曲
 */
class DailyRecommendActivity : BaseActivity() {

    private lateinit var binding: ActivityPlayListDetailBinding

    private val adapter = PlayListAdapter()

    private val songBarHelper = SongBarHelper()

    private val viewModel: DailyRecommendViewModel by lazy {
        ViewModelProvider(this).get(DailyRecommendViewModel::class.java)
    }

    /**
     * 是否已传入播放歌单
     */
    private var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 获取传来的数据
//        val data = intent.getStringExtra("songList")
//        songList = Gson().fromJson<List<Song>>(data,object :TypeToken<List<Song>>(){}.type)

        // 设置顶部bar的渐变动画
        bar.viewTreeObserver.addOnGlobalLayoutListener {
            consecutiveLayout.stickyOffset = bar.height
            showView.viewTreeObserver.addOnGlobalLayoutListener {
                consecutiveLayout.setOnVerticalScrollChangeListener { v, scrollY, oldScrollY, scrollState ->
                    barBackgroundView.alpha = (scrollY*1.0 /(showView.height-bar.height) ).toFloat()
                }
            }

        }
        // 初始化RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.setDiffCallback(SongDiffCallback())
        recyclerView.adapter = adapter

        viewModel.songList.observe(this){
            // 设置歌单基本数据
            setPlayListInfo(it[0])
            // 设置歌曲数据
            adapter.setDiffNewData(it)
            playListNumText.text = "（共${it.size}首）"
        }

        // “播放全部” 的点击事件
        playAllView.setOnClickListener {
            val songs = adapter.data
            if(songs.size != 0){
                SongPlayer.play(songs[0].toPlayerSong(),songs.toPlayerSongList())
            }
        }

        // 设置RecyclerView 点击事件
        adapter.setOnItemClickListener { _, _, position ->
            val songList = adapter.data
            val song = songList[position]
            if(!flag){
                SongPlayer.play(song.toPlayerSong(),songList.toPlayerSongList())
                flag = true
            }else{
                SongPlayer.play(song.toPlayerSong())
            }
        }
        // 更多按钮的点击事件
        adapter.addChildClickViewIds(R.id.moreButton)
        adapter.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.moreButton){

                val songList = this.adapter.data
                val song = songList[position]

                val dialog = SongOpDialog(this,song.toPlayerSong())
                dialog.show()
            }
        }

    }

    /**
     * 设置歌单基本数据
     */
    private fun setPlayListInfo(song: Song){
        toolbarName.text = "每日推荐"
        // 设置歌单名
        playListName.text = "每日推荐歌曲"
        // 设置背景图
        showView.width { showViewWidth ->
            barBackgroundView.height { barBackgroundViewHeight ->
                playAllBackgroundImg.height { playAllBackgroundImgHeight ->
                    Glide.with(this)
                            .asBitmap()
                            .load(song.al.picUrl)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    Glide.with(this@DailyRecommendActivity)
                                            .load(resource)
                                            .into(playListCoverImg)
                                    val bitmaps = convert(resource, showView.width,
                                            barBackgroundView.height, showView.height,
                                            playAllBackgroundImg.height)
                                    Glide.with(this@DailyRecommendActivity)
                                            .load(bitmaps[0])
                                            .into(barBackgroundView)

                                    Glide.with(this@DailyRecommendActivity)
                                            .load(bitmaps[1])
                                            .into(showViewBackgroundImg)

                                    Glide.with(this@DailyRecommendActivity)
                                            .load(bitmaps[2])
                                            .into(playAllBackgroundImg)
                                    barBackgroundView.colorFilter = PorterDuffColorFilter(
                                            Color.rgb(123,123,123),
                                            PorterDuff.Mode.MULTIPLY
                                    )
                                    showViewBackgroundImg.colorFilter = PorterDuffColorFilter(
                                            Color.rgb(123,123,123),
                                            PorterDuff.Mode.MULTIPLY
                                    )
                                    playAllBackgroundImg.colorFilter = PorterDuffColorFilter(
                                            Color.rgb(123,123,123),
                                            PorterDuff.Mode.MULTIPLY
                                    )
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}

                            })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        songBarHelper.setSongBar(this,songBar)
    }

    override fun onStop() {
        super.onStop()
        songBarHelper.release()
    }

    /**
     * 把歌单图转换为 3 张图片
     */
    private fun convert(source: Bitmap, screenWidth: Int,
                        barHeight: Int, showHeight: Int, bottomHeight: Int): Array<Bitmap> {
        val blurBitmap: Bitmap = EasyBlur.with(this)
                .bitmap(source)
                .radius(25)
                .scale(16)
                .blur()
        // 缩放图片
        val width = blurBitmap.width
        val height = blurBitmap.height
        val newHeight = showHeight + bottomHeight
        val scale: Float
        scale = if (newHeight * 1.0 / screenWidth > height * 1.0 / width) {
            (newHeight * 1.0 / height).toFloat()
        } else {
            screenWidth.toFloat() / width
        }
//        Log.e("tag","screenWidth=${screenWidth},newHeight=${newHeight}")
//        Log.e("tag","width = ${width},height=${height},scale = ${scale}")
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        val scaleBitmap = Bitmap.createBitmap(blurBitmap, 0, 0, width, height, matrix, false)
        blurBitmap.recycle()
        // 从scaleBitmap截取三张图片
        var barBitmap = Bitmap.createBitmap(scaleBitmap, 0, showHeight - barHeight, screenWidth, barHeight)
        var showBitmap = Bitmap.createBitmap(scaleBitmap, 0, 0, screenWidth, showHeight)
        var bottomBitmap = Bitmap.createBitmap(scaleBitmap, 0, showHeight, screenWidth, bottomHeight)
        scaleBitmap.recycle()

        barBitmap = EasyBlur.with(this)
                .bitmap(barBitmap)
                .radius(25)
                .scale(1)
                .blur();
        showBitmap = EasyBlur.with(this)
                .bitmap(showBitmap)
                .radius(25)
                .scale(1)
                .blur();
        bottomBitmap = EasyBlur.with(this)
                .bitmap(bottomBitmap)
                .radius(25)
                .scale(1)
                .blur();
        return arrayOf(barBitmap, showBitmap, bottomBitmap)
    }



}