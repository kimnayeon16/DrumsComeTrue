package com.ssafy.drumscometrue.kpop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.adapter.kPopAdapter
import com.ssafy.drumscometrue.mainpage.PageAdapter
import kotlin.math.abs

class KpopListActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

//    private val backgroundResources = arrayOf(
//        R.drawable.back_img,
//        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kpop_slide_list)

//        val mRecyclerView = findViewById<RecyclerView>(R.id.recyclerview)
//        mRecyclerView.layoutManager = LinearLayoutManager(this)
        viewPager = findViewById(R.id.view_pager)


        initViews()
    }

        private fun initViews() {
            val kpopList = listOf<Kpop>(
                Kpop("곰 세마리", "북극곰", "img_three_bears", "ThreeBears", 6590, 480, 38000, "레벨1", 60),
                Kpop("나비야", "나비", "img_butterfly", "Butterfly", 6517, 480, 42000, "레벨2", 96),
                Kpop("작은별", "작은별", "img_star", "Star", 6293, 683, 48000, "레벨3", 72),
                Kpop("거미가 줄을 타고\n올라갑니다", "거미", "img_spider", "Spider", 4838, 383, 46000, "레벨4", 144)
            )


            val adapter = kPopAdapter(this,kpopList){kpop: Kpop ->
                val intent = Intent(this, KpopPlayActivity::class.java)
                intent.putExtra("song", kpop.song)
                intent.putExtra("artist", kpop.singer)
                intent.putExtra("score", kpop.score)
                intent.putExtra("prelude", kpop.prelude)
                intent.putExtra("interval", kpop.interval)
                intent.putExtra("songTotalTime", kpop.songTotalTime)
                intent.putExtra("level", kpop.level)
                intent.putExtra("totalHit", kpop.totalHit)
                startActivity(intent)
            }
            viewPager.adapter = adapter


//            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    // 페이지가 선택될 때마다 배경을 설정합니다.
//                    viewPager.setBackgroundResource(backgroundResources[position])
//                }
//            })


            viewPager.setPageTransformer(object : ViewPager2.PageTransformer {
                override fun transformPage(page: View, position: Float) {
                    val absPosition = abs(position)

                    // 현재 페이지 확대
                    val scale = if (position == 0f) {
                        1f
                    } else {
                        0.85f + 0.15f * (1 - absPosition)
                    }

                    // 다음 페이지가 살짝 보이도록
                    val translationX = -page.width * 0.3f * position
                    page.translationX = translationX
                    val translationY = 1 - (0.15f * abs(position))
                    page.translationY = translationY
                    // 현재 페이지 확대 및 페이드 인/아웃 효과
                    page.scaleX = scale
                    page.scaleY = scale
                    page.alpha = 1f - 0.25f * absPosition
                }
            })
//        val mAdapter = kPopAdapter(this, kpopList){ kpop ->
//            val intent = Intent(this, KpopPlayActivity::class.java)
//            intent.putExtra("song", kpop.song)
//            intent.putExtra("artist", kpop.singer)
//            intent.putExtra("score", kpop.score)
//            intent.putExtra("prelude", kpop.prelude)
//            intent.putExtra("interval", kpop.interval)
//            intent.putExtra("songTotalTime", kpop.songTotalTime)
//            intent.putExtra("level", kpop.level)
//            startActivity(intent)
////            finish()
//        }
//        mRecyclerView.adapter = mAdapter

    }
}