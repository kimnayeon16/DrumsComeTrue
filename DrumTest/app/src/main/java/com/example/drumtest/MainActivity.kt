package com.example.drumtest

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    // 늦은 초기화
    private lateinit var drumSoundPlayer: DrumSoundPlayer
    private lateinit var btnSetup: BtnSetup

    override fun onCreate(savedInstanceState: Bundle?) {
        // 가로 모드 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drumSoundPlayer = DrumSoundPlayer(this)
        btnSetup = BtnSetup(this, drumSoundPlayer)

    }

    override fun onDestroy() {
        drumSoundPlayer.release()
        super.onDestroy()
    }
}