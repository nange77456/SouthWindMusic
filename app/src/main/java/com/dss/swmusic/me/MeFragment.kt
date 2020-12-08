package com.dss.swmusic.me

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.dss.swmusic.R
import com.dss.swmusic.adapter.MeFunctionGroupAdapter
import com.dss.swmusic.adapter.MePlayListAdapter
import com.dss.swmusic.adapter.diff.PlayListDiffCallback
import com.dss.swmusic.databinding.FragmentMeBinding
import com.dss.swmusic.entity.MeFunctionItem
import com.dss.swmusic.me.viewmodel.MeViewModel
import com.dss.swmusic.network.OkCallback
import com.dss.swmusic.network.ServiceCreator
import com.dss.swmusic.network.UserDataService
import com.dss.swmusic.network.bean.CountResult
import com.dss.swmusic.network.bean.LevelResult
import com.dss.swmusic.network.bean.PlayList
import com.dss.swmusic.network.bean.PlayListResult
import com.dss.swmusic.util.UserBaseDataUtil
import kotlinx.android.synthetic.main.fragment_me.*
import retrofit2.Call
import retrofit2.Response

class MeFragment : Fragment() {

    private var _binding: FragmentMeBinding? = null
    private val binding get() = _binding!!

    private val viewModel:MeViewModel by lazy {
        ViewModelProvider(this).get(MeViewModel::class.java)
    }

    /**
     * 用户基本数据
     */
    val userBaseData = UserBaseDataUtil.getUserBaseData()

    /**
     * 标签组 的数据
     */
    private val functionGroupData = mutableListOf<MeFunctionItem>()


    /**
     * 创建的歌单 的recyclerViewAdapter
     */
    private val createdPlayListAdapter = MePlayListAdapter()

    /**
     * 收藏的歌单 的recyclerViewAdapter
     */
    private val collectedPlayListAdapter = MePlayListAdapter()

    /**
     * 网络请求类
     */
    private val userDataService = ServiceCreator.create<UserDataService>()

    init {
        // 初始化 FunctionGroupData
        functionGroupData.add(MeFunctionItem(R.drawable.ic_download_me, "本地下载"))
        functionGroupData.add(MeFunctionItem(R.drawable.ic_download_me, "本地下载"))
        functionGroupData.add(MeFunctionItem(R.drawable.ic_download_me, "本地下载"))
        functionGroupData.add(MeFunctionItem(R.drawable.ic_download_me, "本地下载"))
        functionGroupData.add(MeFunctionItem(R.drawable.ic_download_me, "本地下载"))
        functionGroupData.add(MeFunctionItem(R.drawable.ic_download_me, "本地下载"))
        functionGroupData.add(MeFunctionItem(R.drawable.ic_download_me, "本地下载"))
        functionGroupData.add(MeFunctionItem(R.drawable.ic_download_me, "本地下载"))

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 设置基本数据的 UI
//        initProfileUI()
        // 设置第一个RecyclerView
        val functionGroupAdapter = MeFunctionGroupAdapter(functionGroupData)
        functionGroupRecyclerView.adapter = functionGroupAdapter
        functionGroupRecyclerView.layoutManager = GridLayoutManager(context, 4)
        functionGroupAdapter.setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> {
                    // 跳转本地音乐
                    val intent = Intent(context, LocalMusicActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        // 设置歌单的RecyclerView
        initPlayList()
        viewModel.userBaseData.observe(this){
            Glide.with(this)
                    .load(it.avatarUrl)
                    .into(profileImage)
            // 设置昵称
            nickNameTextView.text = it.nickname ?: "无昵称"
            // 设置等级
            levelTextView.text = "Lv.${it.level} "
        }

        // 设置“我喜欢的音乐”的ui
        viewModel.likedPlayList.observe(this){
            it?.let{
                Glide.with(this)
                        .load(it.coverImgUrl)
                        .into(likeImageView)
                likeMusicNum.text = "${it.trackCount}首"
            }
        }

        // 设置“我创建的歌单"的ui
        viewModel.createdPlayList.observe(this){
            it?.let{
                Log.e("tag",it.toString())
                createdPlayListAdapter.setDiffNewData(it)
            }
        }
        // 设置”我收藏的歌单"数据更新Ui
        viewModel.collectedPlayList.observe(this){
            it?.let{
                collectedPlayListAdapter.setDiffNewData(it)
            }
        }

        // 设置歌单tab的刷新逻辑
        setPlayListTab()

        // 设置Tab透明度渐变
        consecutiveScrollerLayout.setOnVerticalScrollChangeListener { v, scrollY, oldScrollY, scrollState ->
            barBackgroundView.alpha = (scrollY / 280.0).toFloat()
        }
    }

    /**
     * 初始化“基本数据”的ui
     */
    private fun initProfileUI() {
        // 设置头像
        userBaseData.avatarUrl?.let {
            Glide.with(this)
                    .load(it)
                    .into(profileImage)
        }
        // 设置昵称
        nickNameTextView.text = userBaseData.nickname ?: "无昵称"
        // 发送网络请求获取等级
        userDataService.getLevelInfo(UserBaseDataUtil.getCookie()).enqueue(object : OkCallback<LevelResult>() {
            override fun onSuccess(result: LevelResult) {
                levelTextView.text = "Lv.${result.data.level}"
            }
        })
    }


    /**
     * 初始化歌单的RecyclerView
     */
    private fun initPlayList() {
        createdPlayListAdapter.setDiffCallback(PlayListDiffCallback())
        collectedPlayListAdapter.setDiffCallback(PlayListDiffCallback())
        playListRecyclerView.adapter = createdPlayListAdapter
        playListRecyclerView.layoutManager = GridLayoutManager(context, 2)
    }

    /**
     * 设置歌单 tab 的切换逻辑
     */
    private fun setPlayListTab() {
        createdPlayListTextView.setOnClickListener {
            createdPlayListTextView.setTypeface(null, Typeface.BOLD)
            collectPlayListTextView.setTypeface(null, Typeface.NORMAL)
            createdPlayListTextView.setTextColor(resources.getColor(R.color.colorText))
            collectPlayListTextView.setTextColor(resources.getColor(R.color.colorTextLight))

            playListRecyclerView.adapter = createdPlayListAdapter
            playListRecyclerView.layoutManager = GridLayoutManager(context,2)
//            mePlayListAdapter.setNewInstance(createdPlayList)
        }
        collectPlayListTextView.setOnClickListener {
            createdPlayListTextView.setTypeface(null, Typeface.NORMAL)
            collectPlayListTextView.setTypeface(null, Typeface.BOLD)
            createdPlayListTextView.setTextColor(resources.getColor(R.color.colorTextLight))
            collectPlayListTextView.setTextColor(resources.getColor(R.color.colorText))

            playListRecyclerView.adapter = collectedPlayListAdapter
            playListRecyclerView.layoutManager = GridLayoutManager(context,2)
//            mePlayListAdapter.setNewInstance(collectPlayList)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}