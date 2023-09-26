package com.ssafy.drumscometrue.mainpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.drumscometrue.R

class MainPageActivity : AppCompatActivity() {
    private lateinit var pagerMain: ViewPager2
    private val fragmentArrayList = ArrayList<Fragment>()
    private lateinit var bottomNav: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        pagerMain = findViewById(R.id.pagerMain)
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.toHome

        fragmentArrayList.add(FreePlayingFragment())
        fragmentArrayList.add(PracticeFragment())
        fragmentArrayList.add(TutorialFragment())
        fragmentArrayList.add(LaunchPadFragment())

        val adapterViewPager = AdapterViewPager(this, fragmentArrayList)
        // Adapter 설정
        pagerMain.adapter = adapterViewPager

        pagerMain.orientation = ViewPager2.ORIENTATION_VERTICAL

        // effect
        pagerMain.setPageTransformer { page, position ->
            val absPosition = Math.abs(position)
            val rotation: Float

            if (position >= 0) {
                // 아래로 슬라이드할 때의 회전 각도
                rotation = absPosition * 180
            } else {
                // 위로 슬라이드할 때의 회전 각도
                rotation = -absPosition * 180
            }

            page.rotation = rotation
            page.alpha = 1 - absPosition * 0.5f // 투명도 조절
            page.scaleY = 1 - absPosition * 0.2f // 크기 조절
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.toDrum -> {
                    // 드럼 연주 페이지로 이동
                    val intent = Intent(this, FreePageActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.toHome -> {
                    // 메인 페이지로 이동
                    true
                }

                R.id.toUser -> {
                    // 유저 페이지로 이동
                    val intent = Intent(this, UserPageActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        pagerMain.setPadding(100, 0, 100, 0)
    }
}