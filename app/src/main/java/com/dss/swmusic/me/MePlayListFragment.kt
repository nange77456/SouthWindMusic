package com.dss.swmusic.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dss.swmusic.adapter.MePlayListAdapter
import com.dss.swmusic.databinding.FragmentMePlayListBinding
import com.dss.swmusic.network.bean.PlayList
import kotlinx.android.synthetic.main.fragment_me_play_list.*

class MePlayListFragment :Fragment(){

    private var _binding: FragmentMePlayListBinding? = null
    private val binding get() = _binding!!

    /**
     * RecyclerView Adapter
     */
    private val mePlayListAdapter: MePlayListAdapter = MePlayListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMePlayListBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 设置RecyclerView
        recyclerView.adapter = mePlayListAdapter
        recyclerView.layoutManager = GridLayoutManager(context,2)
        // 设置RecyclerView 数据
        setRecyclerViewData()
    }

    private fun setRecyclerViewData(){
        val itemList = mutableListOf<PlayList>()

        mePlayListAdapter.setNewInstance(itemList)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}