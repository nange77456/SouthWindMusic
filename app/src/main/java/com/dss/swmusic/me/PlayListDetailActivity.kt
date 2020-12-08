package com.dss.swmusic.me

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dss.swmusic.databinding.ActivityPlayListDetailBinding
import kotlinx.android.synthetic.main.activity_play_list_detail.*

class PlayListDetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPlayListDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bar.viewTreeObserver.addOnGlobalLayoutListener {
            consecutiveLayout.stickyOffset = bar.height
        }
    }

    override fun onResume() {
        super.onResume()

    }
}