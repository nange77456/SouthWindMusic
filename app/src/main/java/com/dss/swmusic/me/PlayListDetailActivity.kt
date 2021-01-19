package com.dss.swmusic.me

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dss.swmusic.BaseActivity
import com.dss.swmusic.R
import com.dss.swmusic.adapter.PlayListAdapter
import com.dss.swmusic.adapter.diff.SongDiffCallback
import com.dss.swmusic.custom.dialog.BottomSheetDialog
import com.dss.swmusic.databinding.ActivityPlayListDetailBinding
import com.dss.swmusic.discover.DailyRecommendActivity
import com.dss.swmusic.event.PlayListUpdateEvent
import com.dss.swmusic.me.viewmodel.PlayListDetailViewModel
import com.dss.swmusic.me.viewmodel.PlayListDetailViewModelFactory
import com.dss.swmusic.util.*
import com.zhouwei.blurlibrary.EasyBlur
import kotlinx.android.synthetic.main.activity_play_list_detail.*
import kotlinx.android.synthetic.main.activity_play_list_detail.barBackgroundView
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.item_play_list.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PlayListDetailActivity : BaseActivity() {

    private final val TAG = "PlayListDetailActivity"

    private lateinit var binding:ActivityPlayListDetailBinding

    private lateinit var viewModel:PlayListDetailViewModel

    private val adapter = PlayListAdapter()

    private val songBarHelper = SongBarHelper()

    /**
     * 是否已传入播放歌单
     */
    private var flag = false

    private var playListId:Long = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EventBus.getDefault().register(this)

        // 设置返回按钮
        icon_cancel.setOnClickListener {
            finish()
        }

        // 获取歌单的id，从上一个Activity传来
        playListId = intent.getLongExtra(KEY, 0)

        // 初始化ViewModel
        viewModel = PlayListDetailViewModelFactory(playListId).create(PlayListDetailViewModel::class.java)

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

        // 监听歌单基本数据变化
        viewModel.playListDetail.observe(this){
            // 设置歌单名
            playListName.text = it.name
            // 设置歌单创建者的头像
            Glide.with(this)
                    .load(it.creator.avatarUrl)
                    .into(createrAvatarImg)
            // 设置歌单创建者的名字
            createrName.text = it.creator.nickname
            // 设置歌单描述
            playListDesc.text = it.description
            // 设置背景图
            showView.width{ showViewWidth->
                barBackgroundView.height { barBackgroundViewHeight->
                    playAllBackgroundImg.height { playAllBackgroundImgHeight->
                        Glide.with(this)
                                .asBitmap()
                                .load(it.coverImgUrl)
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                        Glide.with(this@PlayListDetailActivity)
                                                .load(resource)
                                                .into(playListCoverImg)
                                        val bitmaps = convert(resource, showView.width,
                                                barBackgroundView.height, showView.height,
                                                playAllBackgroundImg.height)
                                        Glide.with(this@PlayListDetailActivity)
                                                .load(bitmaps[0])
                                                .into(barBackgroundView)
                                        Glide.with(this@PlayListDetailActivity)
                                                .load(bitmaps[1])
                                                .into(showViewBackgroundImg)
                                        Glide.with(this@PlayListDetailActivity)
                                                .load(bitmaps[2])
                                                .into(playAllBackgroundImg)
//                                        barBackgroundView.colorFilter = PorterDuffColorFilter(
//                                                Color.rgb(123,123,123),
//                                                PorterDuff.Mode.MULTIPLY
//                                        )
//                                        showViewBackgroundImg.colorFilter = PorterDuffColorFilter(
//                                                Color.rgb(123,123,123),
//                                                PorterDuff.Mode.MULTIPLY
//                                        )
//                                        playAllBackgroundImg.colorFilter = PorterDuffColorFilter(
//                                                Color.rgb(123,123,123),
//                                                PorterDuff.Mode.MULTIPLY
//                                        )
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {}

                                })
                    }
                }
            }




        }

        // 监听歌曲数据变化
        viewModel.songs.observe(this){
            Log.e(TAG, "onCreate: 歌曲数据变化,size = ${it.size},songs = $it" )
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
            val songList = viewModel.songs.value!!
            val song = songList[position]
            Log.e(TAG, " 点击歌曲：$song")
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
            Log.e(TAG, "onCreate: click item", )
            if(view.id == R.id.moreButton){

                val songList = viewModel.songs.value!!
                val song = songList[position]

                val dialog = SongOpDialog(this,song.toPlayerSong(),playListId)
                dialog.show()
            }
        }

    }

    /**
     * 歌单更新时重新请求数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlaylistUpdateEvent(event:PlayListUpdateEvent){
        if(event.updatePlayListId == playListId){
            viewModel.requestData()
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

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    companion object{

        const val KEY = "PLAYLIST_ID"

        fun start(activity: Activity, id: Long){
            val intent = Intent(activity, PlayListDetailActivity::class.java)
            intent.putExtra(KEY, id)
            activity.startActivity(intent)
        }
    }
}