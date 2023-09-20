package com.ssafy.drumscometrue.kpop

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.ssafy.drumscometrue.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class KpopPlayActivity : AppCompatActivity() {

    private var mediaPlayer : MediaPlayer ?= null


    private fun readJsonFromAssets(fileName: String): String {
        val json: String
        try {
            val inputStream: InputStream = assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            json = stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
        return json
    }

    private val handler = Handler()
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kpop_play)

//        mediaPlayer = MediaPlayer.create(this, R.raw.threebears)
//        mediaPlayer?.start()

        textView = findViewById(R.id.myRhythm)

        val json = readJsonFromAssets("ThreeBears.json")
        val jsonObject = JSONObject(json)
        val threeBears = jsonObject.getJSONArray("ThreeBears").getJSONObject(0)

        // 1초마다 UI 업데이트
        val intervalMillis = 1000L
        val startTime = 0.0
        var currentTime = startTime

        handler.postDelayed(object : Runnable {
            override fun run() {
                val notesArray = mutableListOf<String>()
                val jsonArray = threeBears.optJSONArray(currentTime.toString())
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        val value = jsonArray.getString(i)
                        notesArray.add(value)
                    }
                }

// notesArray를 문자열 배열로 변환
                val notes = notesArray.toTypedArray()


// 이제 notes를 사용하여 joinToString 함수를 호출할 수 있습니다.
                textView.text = notes.joinToString(", ")
                System.out.println(textView.text)

                // 다음 시간으로 이동
                currentTime += 0.01

                // 종료 시간 설정
                val endTime = 50.0
                if (currentTime <= endTime) {
                    handler.postDelayed(this, intervalMillis)
                }else{
                    mediaPlayer = MediaPlayer.create(this@KpopPlayActivity, R.raw.threebears)
                    mediaPlayer?.start()
                }
            }
        }, intervalMillis)

        handler.postDelayed({
            textView.text = "3"
            handler.postDelayed({
                textView.text = "2"
                handler.postDelayed({
                    textView.text = "1"
                    handler.postDelayed({
                        textView.text = "Start"
                    }, 1000) // 1 초 대기
                }, 1000) // 1 초 대기
            }, 1000) // 1 초 대기
        }, 3000) // 3 초 대기

    }


    override fun onStop(){
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}