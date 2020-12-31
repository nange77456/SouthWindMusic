package com.dss.swmusic.video.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dss.swmusic.network.OkCallback
import com.dss.swmusic.network.ServiceCreator
import com.dss.swmusic.network.VideoService
import com.dss.swmusic.network.bean.RecommendVideoResult
import com.dss.swmusic.network.bean.VideoData
import com.dss.swmusic.util.UserBaseDataUtil
import retrofit2.Call
import retrofit2.Response

class VideoViewModel: ViewModel() {

    private val videoService = ServiceCreator.create<VideoService>()

    /**
     * 分页
     */
    private var offset = 0

    val recommendVideos = MutableLiveData<MutableList<VideoData>>()

    init {

        queryRecommendVideos(0)
    }

    /**
     * 请求推荐视频
     */
    private fun queryRecommendVideos(offset: Int){
        videoService.getRecommendVideo(offset,
                UserBaseDataUtil.getCookie(),
                System.currentTimeMillis())
                .enqueue(object :OkCallback<RecommendVideoResult>(){

                    override fun onSuccess(result: RecommendVideoResult) {
//                        Log.e("tag","success ${result.datas}")
                        var data = recommendVideos.value
                        if(data == null){
                            data = mutableListOf()
                        }
                        data.addAll(result.datas)
                        recommendVideos.value = data
                    }
                })
    }

    /**
     * 请求下一页数据
     */
    fun queryNextVideos(){
        offset++
        queryRecommendVideos(offset)
    }

    /**
     * 刷新数据
     */
    fun refreshVideos(){
        offset = 0
        if(recommendVideos.value != null){
            recommendVideos.value!!.clear()
        }
        queryRecommendVideos(offset)
    }

}