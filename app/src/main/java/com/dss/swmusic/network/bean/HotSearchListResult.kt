package com.dss.swmusic.network.bean

import com.google.gson.annotations.SerializedName

data class HotSearchListResult(
    var result:Result
):Result(){
    data class Result(
        var hots: MutableList<Hot>
    )

    data class Hot(
            @SerializedName("first")
            var searchName:String
    )

}
