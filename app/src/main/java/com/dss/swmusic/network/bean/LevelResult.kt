package com.dss.swmusic.network.bean

/**
 * "获取用户等级“的网络请求的 返回结果数据类
 */
data class LevelResult(
        /**
         * 200 代表没问题
         */
        var code: Int,
        var data: Data

){
    data class Data(
            var level: Int
    )
}
