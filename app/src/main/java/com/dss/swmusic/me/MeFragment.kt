package com.dss.swmusic.me

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.dss.swmusic.R
import com.dss.swmusic.adapter.MeFunctionGroupAdapter
import com.dss.swmusic.adapter.MePlayListAdapter
import com.dss.swmusic.databinding.FragmentMeBinding
import com.dss.swmusic.entity.MeFunctionItem
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

    /**
     * 用户基本数据
     */
    val userBaseData = UserBaseDataUtil.getUserBaseData()

    /**
     * 标签组 的数据
     */
    private val functionGroupData = mutableListOf<MeFunctionItem>()

    /**
     * 我创建的歌单数据
     */
    private val createdPlayList = mutableListOf<PlayList>()

    /**
     * 我收藏的歌单数据
     */
    private val collectPlayList = mutableListOf<PlayList>()

    /**
     * 我喜欢的音乐 歌单
     */
    private var likedPlayList: PlayList? = null

    /**
     * 歌单的RecyclerView adapter
     */
    private val mePlayListAdapter = MePlayListAdapter()

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
        initProfileUI()
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
        // 初始化“我创建的歌单”的数据
        requestPlayListData {
            Log.e("tag", "size = ${collectPlayList.size}")
            mePlayListAdapter.setNewInstance(createdPlayList)
            // 初始化”我喜欢的音乐“歌单数据
            likedPlayList?.let {
                Glide.with(this)
                        .load(it.coverImgUrl)
                        .into(likeImageView)
                likeMusicNum.text = "${it.trackCount}首"
            }
        }

        setPlayListTab()

        consecutiveScrollerLayout.setOnVerticalScrollChangeListener { v, scrollY, oldScrollY, scrollState ->
//            Log.e("tag", "scrollY = $scrollY,oldScrollY = $oldScrollY , scrollState = $scrollState")

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
     * 获取歌单数据
     */
    private fun requestPlayListData(okCallback: () -> Unit) {
        userDataService.getPlayListCount(UserBaseDataUtil.getCookie()).enqueue(object : OkCallback<CountResult>() {


            override fun onSuccess(countResult: CountResult) {

                userDataService.getUserPlayListInfo(UserBaseDataUtil.getCookie(), userBaseData.uid,
                        countResult.createdPlaylistCount + countResult.subPlaylistCount)
                        .enqueue(object : OkCallback<PlayListResult>() {

                            override fun onSuccess(result: PlayListResult) {
                                likedPlayList = result.playlist[0]
                                for (i in 1 until countResult.createdPlaylistCount) {
                                    createdPlayList.add(result.playlist[i])
//                                            Log.e("tag","url = ${playListResult.playlist[i].coverImgUrl}")
                                }
                                for (i in countResult.createdPlaylistCount until (countResult.createdPlaylistCount
                                        + countResult.subPlaylistCount)) {
                                    collectPlayList.add(result.playlist[i])
                                }
                                okCallback()
                            }
                        })

            }
        })
    }

    /**
     * 初始化歌单的RecyclerView
     */
    private fun initPlayList() {
        playListRecyclerView.adapter = mePlayListAdapter
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

            mePlayListAdapter.setNewInstance(createdPlayList)
        }
        collectPlayListTextView.setOnClickListener {
            createdPlayListTextView.setTypeface(null, Typeface.NORMAL)
            collectPlayListTextView.setTypeface(null, Typeface.BOLD)
            createdPlayListTextView.setTextColor(resources.getColor(R.color.colorTextLight))
            collectPlayListTextView.setTextColor(resources.getColor(R.color.colorText))

            mePlayListAdapter.setNewInstance(collectPlayList)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}