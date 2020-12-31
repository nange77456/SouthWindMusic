package com.dss.swmusic.video

import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dss.swmusic.R
import com.dss.swmusic.adapter.VideoAdapter
import com.dss.swmusic.adapter.diff.VideoDiffCallback
import com.dss.swmusic.databinding.FragmentVideoBinding
import com.dss.swmusic.video.viewmodel.VideoViewModel
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment:Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoViewModel by lazy {
        ViewModelProvider(this).get(VideoViewModel::class.java)
    }

    private val adapter = VideoAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter.setDiffCallback(VideoDiffCallback())
        videoRecyclerView.adapter = adapter
        val layoutManger = LinearLayoutManager(context)
        videoRecyclerView.layoutManager = layoutManger

        // 设置 RefreshHeader 的颜色
        context?.let {
            refreshHeader.setColorSchemeColors(ContextCompat.getColor(it,R.color.colorTheme))
        }
        // 设置上拉加载的loading图片
        Glide.with(footerImg)
                .load(R.drawable.loading)
                .into(footerImg)

        // 监听视频数据变化
        viewModel.recommendVideos.observe(this){
            adapter.setNewInstance(it)
            adapter.notifyDataSetChanged()
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
        }

        refreshLayout.setOnRefreshListener {
            viewModel.refreshVideos()
        }

        refreshLayout.setOnLoadMoreListener {
            viewModel.queryNextVideos()
        }

        // 播放视频划出页面则停止播放
        videoRecyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {

            var firstVisibleItem: Int = 0
            var lastVisibleItem: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                firstVisibleItem = layoutManger.findFirstVisibleItemPosition()
                lastVisibleItem = layoutManger.findLastVisibleItemPosition()
                //大于0说明有播放
                //大于0说明有播放
                if (GSYVideoManager.instance().playPosition >= 0) {
                    //当前播放的位置
                    val position = GSYVideoManager.instance().playPosition
                    //对应的播放列表TAG
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {

                        //如果滑出去了上面和下面就是否，和今日头条一样
                        //是否全屏
                        if (!GSYVideoManager.isFullState(activity)) {
                            GSYVideoManager.releaseAllVideos()
                            adapter.notifyItemChanged(position)
                        }
                    }
                }
            }
        })

    }

    /**
     * 滑动到顶部
     */
    fun scrollToTop(){
        videoRecyclerView.smoothScrollToPosition(0)
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume(false);
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }
}