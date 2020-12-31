package com.dss.swmusic.discover.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dss.swmusic.network.DiscoverService
import com.dss.swmusic.network.OkCallback
import com.dss.swmusic.network.ServiceCreator
import com.dss.swmusic.network.bean.DailyRecommendSongsResult
import com.dss.swmusic.network.bean.Song
import com.dss.swmusic.util.UserBaseDataUtil
import retrofit2.Call
import retrofit2.Response

class DailyRecommendViewModel :ViewModel(){

    private val discoverService = ServiceCreator.create<DiscoverService>()

    val songList = MutableLiveData<MutableList<Song>>()

    init {
        requestData()
    }

    fun requestData(){
        discoverService.getDailyRecommendSongs(UserBaseDataUtil.getCookie())
                .enqueue(object :OkCallback<DailyRecommendSongsResult>(){


                    override fun onSuccess(result: DailyRecommendSongsResult) {
                        songList.value = result.data.dailySongs
                    }

                })
    }

}