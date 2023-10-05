package com.ssafy.drumscometrue.practice

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.kpop.KpopListActivity
import com.ssafy.drumscometrue.partPlay.PadPlayActivity

class PracticeMainActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var btn: Button
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_main)

        // VideoView 초기화 및 동영상 재생
        videoView = findViewById(R.id.videoView)
        textView = findViewById(R.id.explain_text)
        btn = findViewById(R.id.practice_move_btn)

        playVideo(R.raw.back_video_prac, "Practice")

        val navBottom = findViewById<BottomNavigationView>(R.id.nav_bottom)
        navBottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.to_practice -> {
                    playVideo(R.raw.back_video_prac, "Practice")
                    true
                }

                R.id.to_snare -> {
                    playVideo(R.raw.back_video_snare, "Snare")
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

    private fun playVideo(videoResId: Int, text: String) {
        val videoUri = Uri.parse("android.resource://$packageName/$videoResId")
        videoView.setVideoURI(videoUri)

        videoView.setOnPreparedListener { mediaPlayer ->
            val videoRatio = mediaPlayer.videoWidth / mediaPlayer.videoHeight.toFloat()
            val screenRatio = videoView.width / videoView.height.toFloat()
            val scaleX = videoRatio / screenRatio
            if (scaleX >= 1f) {
                videoView.scaleX = scaleX
            } else {
                videoView.scaleY = 1f / scaleX
            }
            videoView.start()
        }

        // TextView의 텍스트 변경
        if(text == "Practice") {
            textView.text = "Kpop 음악과 악보를 통해 \n게임을 즐기듯 연습하세요!\n"
        } else {
            textView.text = "메트로놈과 스네어로 \n박자감각을 익혀보세요!\n"
        }

        textView.gravity = Gravity.CENTER

        // 버튼의 클릭 리스너 설정
        btn.setOnClickListener {
            val intent = if (text == "Practice") {
                Intent(this, KpopListActivity::class.java)
            } else {
                Intent(this, PadPlayActivity::class.java)
            }
            startActivity(intent)
        }
    }

}
