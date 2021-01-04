package com.dss.swmusic.network.bean

data class DefaultSearchKeyResult(
    var data:DefaultSearchKey
) : Result()

data class DefaultSearchKey(
        var showKeyword: String,
        var realkeyword: String,
        var searchType: Int
)
