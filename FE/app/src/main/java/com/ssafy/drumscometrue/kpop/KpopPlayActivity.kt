package com.ssafy.drumscometrue.kpop

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import com.ssafy.drumscometrue.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Timer
import kotlin.concurrent.timer

class KpopPlayActivity : AppCompatActivity() {

    private var mediaPlayer : MediaPlayer ?= null

    private var startTime = 5
    private var songTime = 0
    private var timerTask: Timer ?= null
    private var timerSong: Timer ?= null
    private lateinit var handler: Handler
    private lateinit var countTime: TextView
    private var myRhythm: TextView ?= null

    private var drumData: JSONArray? = null

    private var iDuration: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kpop_play)

        countTime = findViewById(R.id.countTime)
        myRhythm = findViewById(R.id.myRhythm)
        handler = Handler()

        val jsonString = assets.open("ThreeBears.json").reader().readText()
        Log.d("JSON STR", jsonString)

        drumData  = JSONArray(jsonString)
        Log.d("json str", drumData.toString())

        startCountdown()

        timerSong = timer(period = 10) {
            songTime += 10 // songTime을 1/1000초 단위로 증가
            handleDrumData() // 현재 시간에 해당하는 drum 데이터 처리
        }
    }

    //곡 연주 시작 전 카운트다운
    private fun startCountdown() {
        startTime = startTime.toString().toInt()*100
        timerTask = timer(period = 10){
            val sec = startTime / 100
            runOnUiThread {
                if (sec > 1) {
                    countTime.text = "${sec-1}"
                } else if (sec == 1){
                    countTime.text = "Start"
                } else{
                    countTime.text = ""
                    timerTask?.cancel()
                    mediaPlayer = MediaPlayer.create(this@KpopPlayActivity, R.raw.threebears)
                    mediaPlayer?.start()
                    handleDrumData()
                }
            }
            startTime--
        }
    }

    private fun handleDrumData() {
        val drumMap = mutableMapOf<Double, List<String>>()
        for (i in 0 until drumData!!.length()) {
            val entry = drumData!!.getJSONObject(i)
            val entryTime = entry.getString("time").toDouble()
            val drumValuesArray = entry.getJSONArray("drum")

            val drumValuesList = mutableListOf<String>()
            for (j in 0 until drumValuesArray.length()) {
                val drumValue = drumValuesArray.optString(j, "")
                drumValuesList.add(drumValue)
            }
            drumMap[entryTime] = drumValuesList
        }

        System.out.println(drumMap)

        // 현재 시간을 계산 (songTime은 1/1000초 단위로 증가한다고 가정)
        val currentTime = songTime / 1000.0

        val currentDrumValues = drumMap[currentTime]
        if (currentDrumValues != null) {
            for (drumValue in currentDrumValues) {
                // drum 값을 출력하는 방식을 여기에 구현
                runOnUiThread {
                    // 이 부분에서 drum 값을 표시하도록 UI 업데이트
                    myRhythm?.text = drumValue

                }
            }
        }
    }

    override fun onStop(){
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}