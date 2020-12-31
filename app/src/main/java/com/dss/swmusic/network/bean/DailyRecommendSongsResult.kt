package com.dss.swmusic.network.bean

data class DailyRecommendSongsResult(
        var data: Data
):Result()
{
    data class Data(
            var dailySongs: MutableList<Song>
    )
}