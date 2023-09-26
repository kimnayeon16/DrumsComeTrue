package com.ssafy.drumscometrue.kpop

import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import com.ssafy.drumscometrue.R

object Rhythm {
    fun applyTranslationAnimation(textView: TextView){
        val startX = textView.resources.displayMetrics.widthPixels.toFloat() //시작위치
        val endX = 0f //끝 위치
        val translateAnimation = TranslateAnimation(startX, endX, 0f, 0f)
        translateAnimation.duration = 3000 // 애니메이션 지속 시간 (밀리초) //3000 = 3초
        translateAnimation.fillAfter = true // 애니메이션 종료 후 원래 위치 유지할 것인지 여부
        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // 애니메이션이 시작될 때 실행할 코드
            }

            override fun onAnimationEnd(animation: Animation?) {
                // 애니메이션이 종료될 때 실행할 코드
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // 애니메이션이 반복될 때 실행할 코드
            }
        })

        // TextView에 애니메이션 적용
        textView.startAnimation(translateAnimation)

    }
}