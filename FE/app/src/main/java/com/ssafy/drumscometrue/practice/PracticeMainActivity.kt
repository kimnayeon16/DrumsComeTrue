package com.ssafy.drumscometrue.practice

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.kpop.KpopListActivity
import com.ssafy.drumscometrue.padPlay.PadPlayActivity

class PracticeMainActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_main)

        // VideoView 초기화 및 동영상 재생
        videoView = findViewById(R.id.videoView)
        playVideo(R.raw.back_video_prac)

        btn = findViewById(R.id.btn1)
        btn.setOnClickListener {
            val intent = Intent(this, KpopListActivity::class.java)
            startActivity(intent)
        }

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
