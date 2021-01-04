package com.dss.swmusic.me.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchResultViewModelFactory(private val key:String):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchResultViewModel(key) as T
    }
}