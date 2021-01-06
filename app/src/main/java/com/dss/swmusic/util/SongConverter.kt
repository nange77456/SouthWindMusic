package com.dss.swmusic.util

import com.dss.swmusic.entity.Album
import com.dss.swmusic.entity.Artist
import com.dss.swmusic.entity.PlayerSong
import com.dss.swmusic.network.bean.Song


fun Song.toPlayerSong(): PlayerSong {
    // 转换歌手信息
    val artists = mutableListOf<Artist>()
    for (ar in this.ar) {
        val artist = Artist(ar.name)
        artist.id = ar.id
        artists.add(artist)
    }
    // 转换专辑信息
    var album = Album(this.al.name)
    album.picUrl = this.al.picUrl

    // 设置其他信息
    val playerSong = PlayerSong(2, artists, album)
    playerSong.name = this.name
    playerSong.id = this.id
    playerSong.duration = this.duration

    return playerSong
}

fun MutableList<Song>.toPlayerSongList(): MutableList<PlayerSong> {
    val result = mutableListOf<PlayerSong>()
    for (song in this) {
        result.add(song.toPlayerSong())
    }
    return result
}
