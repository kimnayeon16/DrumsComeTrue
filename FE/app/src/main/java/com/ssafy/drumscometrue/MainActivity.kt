package com.ssafy.drumscometrue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.ssafy.drumscometrue.user.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadingIcon = findViewById<ImageView>(R.id.loading_icon)
        val loadingText = findViewById<ImageView>(R.id.loading_text)

        // 애니메이션 리소스 로드
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // 애니메이션 리스너 추가
        fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // 애니메이션 종료 후 LoginActivity로 이동
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        loadingText.startAnimation(fadeInAnimation)
        loadingIcon.startAnimation(fadeInAnimation)

    }
}
