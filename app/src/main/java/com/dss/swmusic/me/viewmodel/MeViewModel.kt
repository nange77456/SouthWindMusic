package com.dss.swmusic.me.viewmodel

import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dss.swmusic.cache.MeCache
import com.dss.swmusic.entity.UserBaseData
import com.dss.swmusic.network.OkCallback
import com.dss.swmusic.network.ServiceCreator
import com.dss.swmusic.network.UserDataService
import com.dss.swmusic.network.bean.CountResult
import com.dss.swmusic.network.bean.PlayList
import com.dss.swmusic.network.bean.PlayListResult
import com.dss.swmusic.network.bean.UserDetailResult
import com.dss.swmusic.util.UserBaseDataUtil

/**
 * MeFragment 的ViewModel
 */
class MeViewModel: ViewModel() {

    /**
     * 网络请求类
     */
    private val userDataService = ServiceCreator.create<UserDataService>()

    /**
     * 缓存类
     */
    private val meCache = MeCache()

    /**
     * 用户基本数据
     */
    val userBaseData =  MutableLiveData<UserBaseData>()

    /**
     * “我喜欢的音乐"歌单
     */
    val likedPlayList = MutableLiveData<PlayList>()

    /**
     * 用户创建的歌单数据（不包括“我喜欢的音乐”）
     */
    val createdPlayList = MutableLiveData<MutableList<PlayList>>()

    /**
     * 用户收藏的歌单数据
     */
    val collectedPlayList = MutableLiveData<MutableList<PlayList>>()

    init {
        // 先从缓存加载数据
        userBaseData.value = UserBaseDataUtil.getUserBaseData()
        meCache.getLikedPlayListCache {
            likedPlayList.value = it
        }
        meCache.getCreatedPlayList {
            createdPlayList.value = it
//            collectedPlayList.value = it
        }
        meCache.getCollectedPlayList {
            collectedPlayList.value = it
//            createdPlayList.value = it
        }
        // 发送网络请求获取数据
        requestPlayListData()
        requestUserDetail()
    }


    /**
     * 网络请求获取歌单数据
     */
    fun requestPlayListData() {
        val uid = userBaseData.value!!.uid

        userDataService.getPlayListCount(UserBaseDataUtil.getCookie())
                .enqueue(object : OkCallback<CountResult>() {


            override fun onSuccess(countResult: CountResult) {

                userDataService.getUserPlayListInfo(UserBaseDataUtil.getCookie(), uid,
                        countResult.createdPlaylistCount + countResult.subPlaylistCount)
                        .enqueue(object : OkCallback<PlayListResult>() {

                            override fun onSuccess(result: PlayListResult) {
                                val createdPlayListTemp = mutableListOf<PlayList>()
                                for (i in 1 until countResult.createdPlaylistCount) {
                                    createdPlayListTemp.add(result.playlist[i])
                                }

                                val collectedPlayListTemp = mutableListOf<PlayList>()
                                for (i in countResult.createdPlaylistCount until (countResult.createdPlaylistCount
                                        + countResult.subPlaylistCount)) {
                                    collectedPlayListTemp.add(result.playlist[i])
                                }

                                // 设置数据
                                likedPlayList.value = result.playlist[0]
                                createdPlayList.value = createdPlayListTemp
                                collectedPlayList.value = collectedPlayListTemp
                                // 重新缓存
                                meCache.cacheLikedPlayList(result.playlist[0])
                                meCache.cacheCreatedPlayList(createdPlayListTemp)
                                meCache.cacheCollectedPlayList(collectedPlayListTemp)
                            }
                        })

            }
        })
    }

    /**
     * 网络请求获取用户数据
     */
    fun requestUserDetail(){
        val uid = userBaseData.value!!.uid
        val cookie = userBaseData.value!!.cookie!!
        userDataService.getUserDetail(uid,cookie).enqueue(object: OkCallback<UserDetailResult>(){
            override fun onSuccess(result: UserDetailResult) {
                val userData:UserBaseData = UserBaseData(uid,cookie)
                userData.gender = result.profile.gender
                userData.nickname = result.profile.nickname
                userData.birthday = result.profile.birthday
                userData.avatarUrl = result.profile.avatarUrl
                userData.backgroundUrl = result.profile.backgroundUrl
                userData.signature = result.profile.signature
                userData.level = result.level

                // 重设数据
                userBaseData.value = userData
                // 刷新缓存
                UserBaseDataUtil.setUserBaseData(userData)
            }
        })
    }

}