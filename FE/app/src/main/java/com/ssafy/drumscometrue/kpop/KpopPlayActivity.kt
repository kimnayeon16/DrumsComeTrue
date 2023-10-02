package com.ssafy.drumscometrue.kpop

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.freePlay.fragment.CameraFragment
import java.util.Timer
import kotlin.concurrent.timer
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.ssafy.drumscometrue.databinding.ActivityKpopPlayBinding


class KpopPlayActivity : AppCompatActivity() {
    //해당 activity의 FrameLayout id
    private lateinit var frameLayout: FrameLayout
    //음원 재생 변수
    private var mediaPlayer : MediaPlayer ?= null
    //곡 시작할 때 3,2,1, start 시간 재기 위한 변수
    private var startTime = 12
    //필요 없는 부분일 수도 있음.
    private var songTime = 0
    private var timerSong: Timer ?= null
    //3,2,1 카운트 및 노래 시작 시간을 위한 timer
    private var timerTask: Timer ?= null

    //곡제목
    private var songName: String ? = null
    private lateinit var binding: ActivityKpopPlayBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_kpop_play)

        binding = ActivityKpopPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val song = intent.getStringExtra("song")
        val score = intent.getStringExtra("score")
        val prelude = intent.getLongExtra("prelude", 0)
        val interval = intent.getLongExtra("interval", 0)
        val songTotalTime = intent.getLongExtra("songTotalTime", 0)
        val level = intent.getStringExtra("level")
        //곡제목 변수에 activity에서 받은 값 넣기
        songName = song

        Handler(Looper.getMainLooper()).postDelayed({
            totalSongTime(songTotalTime)
        }, 12000)

        //KpopListActivity에서 받은 값



        val cameraFragment = CameraFragment()
        //kPopBoardFragment 생성
        val kPopBoardFragment = KPopBoardFragment()
        //KpopBoardFragment로 song, score 전달하기
        val args = Bundle()
        args.putString("song", song)
        args.putString("score", score)
        args.putLong("prelude", prelude)
        args.putLong("interval", interval)
        args.putString("level", level)
        kPopBoardFragment.arguments = args
        //kPopCountFragment 생성
        val kPopCountFragment = KPopCountFragment()

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.board, kPopBoardFragment)
        transaction.add(R.id.find_id_ui_fragment, cameraFragment)
        transaction.add(R.id.count, kPopCountFragment)

        if (!cameraFragment.isAdded && !kPopBoardFragment.isAdded && !kPopCountFragment.isAdded) {
            transaction.commit()
        }

        //frameLayout 초기화
        frameLayout = findViewById(R.id.frameLayout)

        //시작 카운트 및 음악 실행
        startCountdown()

        val songLengthMillis = songTotalTime + 12000// 노래의 길이(밀리초)
        val modalDelayMillis = 6000L // 모달창이 표시된 후 자동 이동까지의 딜레이(밀리초)

        Handler().postDelayed({
            // 모달창 띄우기
            var finishSign: LinearLayout = findViewById(R.id.finishSign)
            var finishSong: TextView = findViewById(R.id.finishSong)
            var progressBar : ProgressBar = findViewById(R.id.progressBar)
            finishSign.visibility = View.VISIBLE
            finishSong.text = "$song"
            //로딩 페이지 - progressBar 타이머 업데이트
            val progressBarUpdateTimer = Timer()
            val progressBarUpdateInterval = 1000L

            val animator = ValueAnimator.ofInt(0, 100)
            animator.duration = 5000 // 5초 동안 애니메이션 실행
            animator.addUpdateListener { animation ->
                val progress = animation.animatedValue as Int
                progressBar.progress = progress
            }

            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null

                    // 타이머 취소
                    timerTask?.cancel()

                    // 애니메이션 종료 후 다른 액티비티로 이동하는 코드 추가
                    val intent = Intent(this@KpopPlayActivity, KpopListActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish() // 현재 액티비티 종료 (선택사항)
                }
            })
            animator.start()
        }, songLengthMillis)
    }

    private fun totalSongTime(songTotalTime: Long) {
        val durationInSeconds = songTotalTime/1000 // 다운로드를 원하는 시간 (초)
        val totalProgressSteps = 100 // 전체 프로그레스 단계
        val updateInterval = (durationInSeconds * 1000) / totalProgressSteps // 업데이트 간격 (밀리초)
        val increment = 1
        val handler = Handler(Looper.getMainLooper())

        var progress = 0

        val progressAnimator = ValueAnimator.ofInt(0, 100)
        progressAnimator.duration = durationInSeconds * 1000L // 총 애니메이션 시간 (밀리초)

        progressAnimator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            binding.progressSong.progress = progress
        }

        progressAnimator.start()
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
        val boardFragment = supportFragmentManager.findFragmentById(R.id.board)
        val cameraFragment = supportFragmentManager.findFragmentById(R.id.find_id_ui_fragment)
        val countFragment = supportFragmentManager.findFragmentById(R.id.count)

        if (boardFragment != null && cameraFragment != null && countFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            if (boardFragment.isAdded) {
                transaction.remove(boardFragment)
            }
            if (cameraFragment.isAdded) {
                transaction.remove(cameraFragment)
            }
            if (countFragment.isAdded) {
                transaction.remove(countFragment)
            }

            transaction.commit()
        }
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        // 타이머 취소
        timerTask?.cancel()

        super.onBackPressed()
    }
}