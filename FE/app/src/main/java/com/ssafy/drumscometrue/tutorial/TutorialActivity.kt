package com.ssafy.drumscometrue.tutorial



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.FragmentTransaction


import androidx.viewpager2.widget.ViewPager2
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.freePlay.FreePlayActivity
import com.ssafy.drumscometrue.freePlay.fragment.CameraFragment


class TutorialActivity : AppCompatActivity() {
    private val videoItems = listOf(
        VideoItem("JV7OcwKU734",R.drawable.tutor_img_snaredrum, "튜토리얼 1", "스네어 연주법","1. 스틱 팁(머리)를 중앙에 둔다\n\n" +
                "2. 이때 스틱은 스네어와 2-3cm 높이로 수평\n\n" +
                "3. 손목을 들어줬다가 내려친다"),
        VideoItem("30ooSe0yoVs", R.drawable.tutor_img_hihatdrum,"튜토리얼 2", "하이햇 연주법", "1.손목에 힘을 풀고 스틱을 가볍게 쥔다 \n\n" +
                "2. 가볍게 손목힘으로 툭툭 친다 \n\n" +
                "3. 하이햇과 스네어를 같이 연주 할 경우 하이햇도 손목을 들어올린다."),
        VideoItem("9iRzQzWkdGY", R.drawable.tutor_img_basedrum,"튜토리얼 2", "베이스드럼 연주법", "1. 연주중이 아닐 땐 비터 베이스에 붙이고 있기 \n\n" +
                "2-1 Heel Up 연주법. \n 발 뒤꿈치를 들고 페달에 발의 앞부분을 붙인 상태로 다리를 살짝 들어서 밟기.\n\n" +
                "2-2 Heel Down 연주법. \n 발 뒤꿈치를 페달에 붙이고 앞발을 들어 페달을 밟기."),
    )

    private var currentPage: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val videoAdapter = VideoAdapter(videoItems)
        viewPager.adapter = videoAdapter
    }
}