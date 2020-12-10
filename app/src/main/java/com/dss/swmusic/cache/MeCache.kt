package com.dss.swmusic.cache

import com.dss.swmusic.network.bean.PlayList
import com.dss.swmusic.util.DiskCacheUtil

/**
 * “首页-我的” 页相关的缓存操作
 */
class MeCache {

    /**
     * 这个类的缓存保存的文件夹
     */
    private val dirName = "/MeCache"

    /**
     * “我喜欢的音乐”的缓存的文件名
     */
    private val LIKED_PLAY_LIST_FILE_NAME = "/likedPlayList"

    /**
     * "我创建的歌单“的缓存的文件名
     */
    private val CREATED_PLAY_LIST_FILE_NAME = "/createdPlayList"

    /**
     * "我收藏的歌单”的缓存的文件名
     */
    private val COLLECTED_PLAY_LIST_FILE_NAME = "/collectedPlayList"

    /**
     * 缓存“我喜欢的音乐”播放列表的数据
     */
    fun cacheLikedPlayList(playList: PlayList){
        DiskCacheUtil.set("$dirName$LIKED_PLAY_LIST_FILE_NAME",playList)
    }

    /**
     * 获取“我喜欢的音乐”播放列表的缓存
     */
    fun getLikedPlayListCache(callback:(PlayList?)->Unit){
        DiskCacheUtil.get<PlayList>("$dirName$LIKED_PLAY_LIST_FILE_NAME"){
            callback(it)
        }
    }

    /**
     * 缓存”我创建的歌单“的数据
     */
    fun cacheCreatedPlayList(playList: List<PlayList>){
        DiskCacheUtil.set("$dirName$CREATED_PLAY_LIST_FILE_NAME",playList)
    }

    /**
     * 获取”我创建的歌单“的缓存
     */
    fun getCreatedPlayList(callback: (MutableList<PlayList>?) -> Unit){
        DiskCacheUtil.get<MutableList<PlayList>>("$dirName$CREATED_PLAY_LIST_FILE_NAME"){
            callback(it)
        }
    }

    /**
     * 缓存“我收藏的歌单”数据
     */
    fun cacheCollectedPlayList(playList: List<PlayList>){
        DiskCacheUtil.set("$dirName$COLLECTED_PLAY_LIST_FILE_NAME",playList)
    }

    /**
     * 获取”我收藏的歌单“的缓存
     */
    fun getCollectedPlayList(callback: (MutableList<PlayList>?) -> Unit){
        DiskCacheUtil.get<MutableList<PlayList>>("$dirName$COLLECTED_PLAY_LIST_FILE_NAME"){
            callback(it)
        }
    }

}