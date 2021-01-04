package com.dss.swmusic.me.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dss.swmusic.network.OkCallback
import com.dss.swmusic.network.SearchService
import com.dss.swmusic.network.ServiceCreator
import com.dss.swmusic.network.bean.SearchSong
import com.dss.swmusic.network.bean.SearchSongResult
import com.dss.swmusic.network.bean.Song

class SearchResultViewModel(private val key:String):ViewModel() {

    private val searchService = ServiceCreator.create<SearchService>()

    /**
     * 歌曲的搜索结果
     */
    val songs  = MutableLiveData<MutableList<Song>>()

    /**
     * 是否有更多数据
     */
    val hasMore = MutableLiveData<Boolean>()

    /**
     * 分页
     */
    private var offset = 0

    init{
        query(0)
    }

    /**
     * 请求数据
     */
    fun query(offset:Int){
        searchService.searchSong(key,offset).enqueue(object :OkCallback<SearchSongResult>(){
            override fun onSuccess(result: SearchSongResult) {
                var data = songs.value
                if(data == null){
                    data = mutableListOf()
                }
                data.addAll(searchSong2Song(result.result.songs))
                songs.value  = data

                if(!result.result.hasMore){
                    hasMore.value = false
                }
            }
        })
    }

    /**
     * 请求下一页数据
     */
    fun queryNextPage(){
        offset ++
        query(offset)
    }

    /**
     * 把SearchSong数据转换为Song类型
     */
    private fun searchSong2Song(searchSongs:List<SearchSong>):MutableList<Song>{
        val result = mutableListOf<Song>()
        for(searchSong in searchSongs){
            val song = Song(searchSong.id,searchSong.name,searchSong.duration,searchSong.artists,searchSong.album)
            result.add(song)
        }
        return result
    }
}