package com.dss.swmusic.network.bean

data class LyricResult(
        var nolyric: Boolean = false,
        var lrc: Lrc
):Result()

data class Lrc(
        var lyric: String
)
