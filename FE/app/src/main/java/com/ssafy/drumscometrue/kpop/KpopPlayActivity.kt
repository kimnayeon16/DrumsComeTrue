package com.ssafy.drumscometrue.kpop

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.freePlay.fragment.CameraFragment
import java.util.Timer
import kotlin.concurrent.timer

class KpopPlayActivity : AppCompatActivity() {
    //해당 activity의 FrameLayout id
    private lateinit var frameLayout: FrameLayout
    //음원 재생 변수
    private var mediaPlayer : MediaPlayer ?= null
    //곡 시작할 때 3,2,1, start 시간 재기 위한 변수
    private var startTime = 5
    //필요 없는 부분일 수도 있음.
    private var songTime = 0
    private var timerSong: Timer ?= null
    //3,2,1 카운트 및 노래 시작 시간을 위한 timer
    private var timerTask: Timer ?= null

    //곡제목
    private var songName: String ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kpop_play)

        //KpopListActivity에서 받은 값
        val song = intent.getStringExtra("song")
        val score = intent.getStringExtra("score")
        val prelude = intent.getLongExtra("prelude", 0)
        val interval = intent.getLongExtra("interval", 0)
        System.out.println("ppppppprrrrrrrrrrrreeeeeeeeeeeeeelllllllllllluuuuuuuuuudddddddddddeeeeeeee $prelude")
        System.out.println("iiiiiiiiinnnnnnnnnntttttttttteeeeeerrrrrrrrvvvvvvvvvvvvaaaaaaalllllll $interval")
        //곡제목 변수에 activity에서 받은 값 넣기
        songName = song


        val cameraFragment = CameraFragment()
        //kPopBoardFragment 생성
        val kPopBoardFragment = KPopBoardFragment()
        //KpopBoardFragment로 song, score 전달하기
        val args = Bundle()
        args.putString("song", song)
        args.putString("score", score)
        args.putLong("prelude", prelude)
        args.putLong("interval", interval)
        kPopBoardFragment.arguments = args
        //kPopCountFragment 생성
        val kPopCountFragment = KPopCountFragment()
        //Fragment 트랜잭션
//        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.camera, cameraFragment)
//        transaction.replace(R.id.board, kPopBoardFragment)
//        transaction.replace(R.id.count, kPopCountFragment)
//        transaction.commit()

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.board, kPopBoardFragment)
        transaction.add(R.id.find_id_ui_fragment, cameraFragment)
        transaction.add(R.id.count, kPopCountFragment)
        transaction.commit()
        //트랜잭션을 완료하고 화면에 변경된 fragment 표시


        //frameLayout 초기화
        frameLayout = findViewById(R.id.frameLayout)

        //시작 카운트 및 음악 실행
        startCountdown()

//        timerSong = timer(period = 10) {
//            songTime += 10 // songTime을 1/1000초 단위로 증가
//        }

//        val hiHatTextView = findViewById<TextView>(R.id.hiHat)
//        Rhythm.applyTranslationAnimation(hiHatTextView)

    }

    //곡 연주 시작 전 카운트다운
    private fun startCountdown() {
        //곡 고르기
        val soundResId: Int = when (songName) {
            "곰 세마리" -> R.raw.threebears
            "나비야" -> R.raw.butterfly
            "Rooftop(옥탑방)" -> R.raw.rooftop
            "거미가 줄을 타고 올라갑니다" -> R.raw.spider
            "작은별" -> R.raw.star
            // 다른 곡들에 대한 리소스 ID도 추가해주세요.
            else -> R.raw.rooftop // 기본값으로 설정할 리소스 ID
        }

        //startTime = 500
        startTime = startTime.toString().toInt()*100
        //0.01초마다 작업 수행
        timerTask = timer(period = 10){
            val sec = startTime / 100
            runOnUiThread {
                if (sec > 1) {
//                    countTime.text = "${sec-1}"
                } else if (sec == 1){
//                    countTime.text = "Start"
                } else{
//                    countTime.text = ""
//                    countTime.setPadding(0, 0, 0, 0)
                    timerTask?.cancel() //timeTask 종료
                    mediaPlayer = MediaPlayer.create(this@KpopPlayActivity, soundResId) //음악 시작
                    mediaPlayer?.start()
                }
            }
            startTime--
        }
    }

    override fun onStop(){
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onBackPressed() {
        // 이전 액티비티로 돌아가기 위한 코드 작성
        mediaPlayer?.release()
        super.onBackPressed()
    }
}