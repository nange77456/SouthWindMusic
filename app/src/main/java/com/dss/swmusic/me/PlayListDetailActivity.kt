package com.dss.swmusic.me

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dss.swmusic.adapter.PlayListAdapter
import com.dss.swmusic.databinding.ActivityPlayListDetailBinding
import com.dss.swmusic.me.viewmodel.PlayListDetailViewModel
import com.dss.swmusic.me.viewmodel.PlayListDetailViewModelFactory
import com.zhouwei.blurlibrary.EasyBlur
import kotlinx.android.synthetic.main.activity_play_list_detail.*

class PlayListDetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPlayListDetailBinding

    private lateinit var viewModel:PlayListDetailViewModel

    private val adapter = PlayListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playListId = intent.getLongExtra(KEY, 0)

        viewModel = PlayListDetailViewModelFactory(playListId).create(PlayListDetailViewModel::class.java)

        bar.viewTreeObserver.addOnGlobalLayoutListener {
            consecutiveLayout.stickyOffset = bar.height
            showView.viewTreeObserver.addOnGlobalLayoutListener {
                consecutiveLayout.setOnVerticalScrollChangeListener { v, scrollY, oldScrollY, scrollState ->
                    barBackgroundView.alpha = (scrollY*1.0 /(showView.height-bar.height) ).toFloat()
                }
            }

        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.playListDetail.observe(this){
            if(it == null){
                return@observe
            }
//            Glide.with(this)
//                    .load(it.coverImgUrl)
//                    .into(playListCoverImg)
            playListName.text = it.name
            Glide.with(this)
                    .load(it.creator.avatarUrl)
                    .into(createrAvatarImg)
            createrName.text = it.creator.nickname
            playListDesc.text = it.description
            // 设置背景图
            Glide.with(this)
                    .asBitmap()
                    .load(it.coverImgUrl)
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            Glide.with(this@PlayListDetailActivity)
                                    .load(resource)
                                    .into(playListCoverImg)
                            val bitmaps = convert(resource,showView.width,barBackgroundView.height,showView.height,playAllBackgroundImg.height)
                            Glide.with(this@PlayListDetailActivity)
                                    .load(bitmaps[0])
                                    .into(barBackgroundView)
                            Glide.with(this@PlayListDetailActivity)
                                    .load(bitmaps[1])
                                    .into(showViewBackgroundImg)
                            Glide.with(this@PlayListDetailActivity)
                                    .load(bitmaps[2])
                                    .into(playAllBackgroundImg)
                            Log.e("tag","bitmap 1 width = ${bitmaps[0].width}")
                            Log.e("tag","bitmap 3 width = ${bitmaps[2].width},${bitmaps[2].height}")
                            Log.e("tag","image view width = ${playAllBackgroundImg.width},${playAllBackgroundImg.height}")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
//                            TODO("Not yet implemented")
                        }

                    })
        }

        viewModel.songs.observe(this){
            adapter.setNewInstance(it)
            playListNumText.text = "（共${it.size}首）"
        }


    }

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

    companion object{

        const val KEY = "PLAYLIST_ID"

        fun start(activity: Activity, id: Long){
            val intent = Intent(activity, PlayListDetailActivity::class.java)
            intent.putExtra(KEY, id)
            activity.startActivity(intent)
        }
    }
}