package com.ssafy.drumscometrue.mainpage

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ScaleInTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val absPosition = Math.abs(position)
        val rotation = if (position >= 0) {
            // 아래로 슬라이드할 때의 회전 각도
            absPosition * 180
        } else {
            // 위로 슬라이드할 때의 회전 각도
            -absPosition * 180
        }
        val alpha = 1 - absPosition * 0.5f // 투명도 조절
        val scaleY = 1 - absPosition * 0.2f // 크기 조절

        page.rotation = rotation
        page.alpha = alpha

    }
}