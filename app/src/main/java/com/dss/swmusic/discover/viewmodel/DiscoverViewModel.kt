package com.dss.swmusic.discover.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dss.swmusic.network.DiscoverService
import com.dss.swmusic.network.OkCallback
import com.dss.swmusic.network.ServiceCreator
import com.dss.swmusic.network.bean.*
import com.dss.swmusic.util.UserBaseDataUtil

/**
 * 发现页的ViewModel
 */
class DiscoverViewModel:ViewModel() {

    private val discoverService = ServiceCreator.create<DiscoverService>()

    /**
     * 轮播图数据
     */
    val bannerData  = MutableLiveData<MutableList<Banner>>()

    /**
     * 推荐歌单数据
     */
    var recommendPlayListData = MutableLiveData<MutableList<RecommendPlayList>>()

    init {
        requestBannerData()
        requestRecommendPlayListData()
    }

    /**
     * 请求轮播图数据
     */
    fun requestBannerData(){
       discoverService.getBanner().enqueue(object :OkCallback<BannerResult>(){

           override fun onSuccess(result: BannerResult) {
                bannerData.value = result.banners
               // TODO 缓存
           }

       })
    }

    /**
     * 请求推荐歌单数据
     */
    fun requestRecommendPlayListData(){
        discoverService.getRecommendPlayList(UserBaseDataUtil.getCookie())
                .enqueue(object :OkCallback<RecommendPlayListResult>(){
                    override fun onSuccess(result: RecommendPlayListResult) {
                        recommendPlayListData.value = result.result
                        // TODO 缓存
                    }
                })
    }

    /**
     * 请求私人雷达的歌单id
     */
    fun requestPersonalRadar(callback:(Long?)->Unit){
        discoverService.getDailyRecommendPlayList(UserBaseDataUtil.getCookie())
                .enqueue(object :OkCallback<DailyRecommendPlayListResult>(){
                    override fun onSuccess(result: DailyRecommendPlayListResult) {
                        val playListId = result.recommend[0].id
                        callback(playListId)
                    }

                    override fun onFailureFinally() {
                        super.onFailureFinally()
                        callback(null)
                    }
                })
    }

}