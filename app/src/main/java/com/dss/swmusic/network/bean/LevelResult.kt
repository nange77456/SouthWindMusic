package com.dss.swmusic.network.bean

/**
 * "获取用户等级“的网络请求的 返回结果数据类
 */
data class LevelResult(
        var data: Data

):Result(){
    data class Data(
            var level: Int
    )
}
