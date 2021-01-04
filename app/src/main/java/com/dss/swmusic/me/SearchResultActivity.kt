package com.dss.swmusic.me

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dss.swmusic.BaseActivity
import com.dss.swmusic.R
import com.dss.swmusic.adapter.PlayListAdapter
import com.dss.swmusic.adapter.diff.SongDiffCallback
import com.dss.swmusic.custom.view.LoadingView
import com.dss.swmusic.databinding.ActivitySearchResultBinding
import com.dss.swmusic.me.viewmodel.SearchResultViewModel
import com.dss.swmusic.me.viewmodel.SearchResultViewModelFactory
import com.dss.swmusic.network.bean.Song
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.video_layout_cover.view.*

class SearchResultActivity : BaseActivity() {

    private lateinit var binding : ActivitySearchResultBinding
    private lateinit var viewModel:SearchResultViewModel

    private val adapter = PlayListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key")!!
        viewModel = SearchResultViewModelFactory(key).create(SearchResultViewModel::class.java)

        // 设置返回按钮
        toolbar.setNavigationOnClickListener {
            finish()
        }
        // 不启用下拉加载
        refreshLayout.setEnableRefresh(false)
        // 启用越界拖拽
        refreshLayout.setEnableOverScrollDrag(true)
        // 设置上拉加载的图片
        Glide.with(this)
                .load(R.drawable.loading)
                .into(footerImg)

        // 设置recyclerView
        val emptyView = LoadingView(this)
        adapter.setEmptyView(emptyView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 监听数据变化
        viewModel.songs.observe(this){
//            Log.e("tag","data size = ${it.size}")
            adapter.addData(it.subList(adapter.data.size,it.size))
            refreshLayout.finishLoadMore()
        }

        viewModel.hasMore.observe(this){
            if(!it){
//                Log.e("tag","no more data")
                footerTextView.text = "没有更多数据了"
                refreshLayout.finishLoadMoreWithNoMoreData()
            }
        }

        adapter.setOnItemClickListener { adapter, view, position ->
//            viewModel.queryNextPage()
        }

        // 上拉加载的监听
        refreshLayout.setOnLoadMoreListener {
            viewModel.queryNextPage()
        }

    }

    companion object{

        fun start(activity: Activity,key:String){
            val intent = Intent(activity,SearchResultActivity::class.java).apply {
                putExtra("key",key)
            }
            activity.startActivity(intent)
        }
    }

}