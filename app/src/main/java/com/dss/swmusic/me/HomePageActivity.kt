package com.dss.swmusic.me

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dss.swmusic.BaseActivity
import com.dss.swmusic.databinding.ActivityHomePageBinding

class HomePageActivity : BaseActivity() {

    private lateinit var binding : ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}