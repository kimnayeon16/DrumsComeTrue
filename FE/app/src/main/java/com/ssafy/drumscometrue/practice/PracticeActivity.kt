package com.ssafy.drumscometrue.practice


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.alertdialog.DialogUtils
import com.ssafy.drumscometrue.freePlay.FreePlayActivity


class PracticeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        val practiceCamera = findViewById<View>(R.id.practiceCamera)
        val practiceMetronome = findViewById<View>(R.id.practiceMetronome)
        val practiceDrum = findViewById<View>(R.id.practiceDrum)

        practiceDrum.setOnClickListener{
            val intent = Intent(this, FreePlayActivity::class.java)
            startActivity(intent)
        }

        practiceMetronome.setOnClickListener{
            val title = "메트로놈 사용법"
            val message = "\uD83D\uDD01 - 소리 변경     ▶️ - 재생     +- - bpm 1씩 증가/감소"
            DialogUtils.showInfoDialog(this, title, message)
        }

        practiceCamera.setOnClickListener {
            val title = "카메라 세팅"
            val message = "카메라 세팅법은 연주 시작 전 가이드라인을 따라주세요!"
            DialogUtils.showInfoDialog(this, title, message)
        }

    }

    private fun showAlertDialog(message: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(message)

        alertDialogBuilder.setPositiveButton("확인") { dialog, _ ->
            // 확인 버튼을 눌렀을 때 수행할 작업 추가
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}