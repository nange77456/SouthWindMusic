package com.dss.swmusic.me

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dss.swmusic.BaseActivity
import com.dss.swmusic.adapter.HotSearchAdapter
import com.dss.swmusic.databinding.ActivitySearchBinding
import com.dss.swmusic.me.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_discover.*

class SearchActivity : BaseActivity() {

    private lateinit var binding :ActivitySearchBinding

    private val adapter = HotSearchAdapter()

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置返回按钮
        returnBtn.setOnClickListener {
            finish()
        }

        // 搜索框获取焦点
        searchEditTextView.requestFocus()

        // 监听默认搜索词数据变化
        viewModel.defaultSearchKey.observe(this){
            searchEditTextView.setHint(it)
        }

        // 设置搜索热词的RecyclerView
        hotSearchRecyclerView.adapter = adapter
        hotSearchRecyclerView.layoutManager = GridLayoutManager(this,2)

        // 监听搜索热词的数据变化
        viewModel.hotSearchList.observe(this){
            adapter.setNewInstance(it)
        }

        // 搜索热词的点击事件
        adapter.setOnItemClickListener { _, _, position ->
            viewModel.hotSearchList.value?.let {
                search(it[position])
            }
        }

        // 搜索按钮的点击事件
        searchEditTextView.setOnSearchListener {
            if(it.equals("") && viewModel.defaultSearchKey.value != null){
                search(viewModel.defaultSearchKey.value!!)
            }else{
                search(searchEditTextView.text)
            }
        }

    }

    private fun search(key:String){
        SearchResultActivity.start(this,key)
    }

}