package com.ssafy.drumscometrue.practice

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.kpop.KpopListActivity
import com.ssafy.drumscometrue.padPlay.PadPlayActivity

class PracticeMainActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_main)

        // VideoView 초기화 및 동영상 재생
        videoView = findViewById(R.id.videoView)
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.back_video_prac)
        playVideo(R.raw.back_video_prac)

        val navBottom = findViewById<BottomNavigationView>(R.id.nav_bottom)
        navBottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.to_practice -> {
                    playVideo(R.raw.back_video_prac)
                    true
                }

                R.id.to_snare -> {
                    playVideo(R.raw.back_video_snare)
                    true
                }

                else -> false

            }
        }

        // 비디오가 끝나면 다시 시작되게 설정
        videoView.setOnCompletionListener {
            videoView.start()
        }


    }

    private fun playVideo(videoResId: Int) {
        val videoUri = Uri.parse("android.resource://$packageName/$videoResId")
        videoView.setVideoURI(videoUri)
        videoView.start()
    }
}


//        practice1Layout.setOnClickListener {
//            val intent = Intent(this, KpopListActivity::class.java)
//            startActivity(intent)
//        }
//
//        practice2Layout.setOnClickListener {
//            val intent = Intent(this, PadPlayActivity::class.java)
//            startActivity(intent)
//        }