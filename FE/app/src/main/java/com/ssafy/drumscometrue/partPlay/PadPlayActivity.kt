package com.ssafy.drumscometrue.partPlay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.drumscometrue.databinding.ActivityPadPlayBinding

class PadPlayActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityPadPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityPadPlayBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
    }

    override fun onBackPressed() {
        finish()
    }
}