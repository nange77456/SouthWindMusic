package com.dss.swmusic.discover

import `fun`.inaction.dialog.dialogs.CommonDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.dss.swmusic.R
import com.dss.swmusic.adapter.BannerAdapter
import com.dss.swmusic.adapter.DiscoverBtnGroupAdapter
import com.dss.swmusic.adapter.DiscoverRecoPlayListAdapter
import com.dss.swmusic.databinding.FragmentDiscoverBinding
import com.dss.swmusic.discover.viewmodel.DiscoverViewModel
import com.dss.swmusic.entity.MeFunctionItem
import com.dss.swmusic.me.PlayListDetailActivity
import com.dss.swmusic.network.bean.Banner
import com.dss.swmusic.network.bean.Song
import com.dss.swmusic.util.MyWebView
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.utils.BannerUtils
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import kotlinx.android.synthetic.main.fragment_discover.*

/**
 * 发现页
 */
class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this).get(DiscoverViewModel::class.java)
    }

    /**
     * 推荐歌单的 recyclerView 的adapter
     */
    private val recommendPlayListAdapter = DiscoverRecoPlayListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Banner 初始配置
        setupBanner()

        // 按钮组 初始配置
        setBtnGroup()

        // 推荐歌单 recyclerVIew 初始配置
        initRecommendPlayListRecyclerView()

        // banner 数据监听
        viewModel.bannerData.observe(this) {
            (bannerViewPager as BannerViewPager<Banner, BannerAdapter.ViewHolder>).refreshData(it)
        }

        // 推荐歌单数据监听
        viewModel.recommendPlayListData.observe(this) {
            recommendPlayListAdapter.setNewInstance(it)
        }

    }

    /**
     * 配置Banner
     */
    private fun setupBanner() {
        bannerViewPager.apply {
            adapter = BannerAdapter()
            setAutoPlay(true)
            setLifecycleRegistry(lifecycle)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setIndicatorSliderGap(getResources().getDimensionPixelOffset(R.dimen.dp_4))
//            setIndicatorMargin(0, 0, 0, resources.getDimension(R.dimen.dp_10).toInt())
            setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
            setIndicatorSliderRadius(resources.getDimension(R.dimen.dp_3).toInt(), resources.getDimension(R.dimen.dp_4_5).toInt())
            setIndicatorSliderColor(ContextCompat.getColor(context, R.color.bannerIndicatorNormalColor),
                    ContextCompat.getColor(context, R.color.bannerIndicatorCheckedColor))
            setOnPageClickListener { clickedView, position ->
                val data = viewModel.bannerData.value?.get(position)
                data?.let { bannerData ->
                    activity?.let { activity ->
                        Log.e("tag", "url = ${bannerData.url}")
                        MyWebView.start(activity, bannerData.url)
                    }
                }
            }
        }.create()
    }

    /**
     * 初始化按钮组
     */
    private fun setBtnGroup() {
        val btnGroup = mutableListOf<MeFunctionItem>()
        with(btnGroup) {
            add(MeFunctionItem(R.drawable.ic_recommend, "每日推荐"))
            add(MeFunctionItem(R.drawable.ic_fm, "私人FM"))
            add(MeFunctionItem(R.drawable.ic_radar, "私人雷达"))
            add(MeFunctionItem(R.drawable.ic_playlist, "歌单"))
            add(MeFunctionItem(R.drawable.ic_ranking_list, "排行榜"))
        }
        val btnGroupAdapter = DiscoverBtnGroupAdapter(btnGroup)
        buttonGroupRecyclerView.adapter = btnGroupAdapter
        buttonGroupRecyclerView.layoutManager = GridLayoutManager(context, 5)
        btnGroupAdapter.setOnItemClickListener { adapter, view, position ->
            when (position) {
                // 每日推荐
                0 -> {
                    activity?.let {
                        val intent = Intent(it, DailyRecommendActivity::class.java)
                        startActivity(intent)
                    }
                }
                // 私人fm
                1 -> {
                    showNoImplementDialog()
                }
                // 私人雷达
                2 -> {
                    viewModel.requestPersonalRadar { playListId ->
                        playListId?.let { id ->
                            activity?.let { activity ->
                                PlayListDetailActivity.start(activity, id)
                            }
                        }
                    }
                }
                // 歌单
                3 -> {
                    showNoImplementDialog()
                }
                // 排行榜
                4 -> {
                    showNoImplementDialog()
                }
            }
        }
    }

    private fun showNoImplementDialog() {
        context?.let {
            val dialog = CommonDialog(it)
            with(dialog) {
                setTitle("提示")
                setContent("暂未实现，下次一定！")
                onConfirmClickListener = {
                    dialog.dismiss()
                }
                onCancelClickListener = {
                    dialog.dismiss()
                }
                show()
            }
        }
    }

    /**
     * 配置 推荐歌单 的 recyclerView
     */
    private fun initRecommendPlayListRecyclerView() {
        recommendPlayListRecyclerView.adapter = recommendPlayListAdapter
        recommendPlayListRecyclerView.layoutManager = GridLayoutManager(context, 3)
        recommendPlayListAdapter.setOnItemClickListener { _, _, position ->
            val playList = viewModel.recommendPlayListData.value!!.get(position)
            activity?.let {
                PlayListDetailActivity.start(it, playList.id)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}