package com.ssafy.drumscometrue.persistentbottom

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.adapter.SlidePagerAdapter
import com.ssafy.drumscometrue.databinding.FragmentPersistentBinding

class PersistentFragment : Fragment(R.layout.fragment_persistent) {

    private lateinit var binding: FragmentPersistentBinding
    lateinit var behavior: BottomSheetBehavior<LinearLayout>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPersistentBinding.bind(view)

        // 초기 indicator 색상 설정
        binding.tabLayout.setSelectedTabIndicatorColor(
            resources.getColor(
                R.color.colorForMusicSelect,
                null
            )
        )

        // ViewPager2 설정
        Log.d("PersistentFragment", "onViewCreated called")
        binding.viewPager.adapter = SlidePagerAdapter(this)

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = if (position == 0) "음악선택" else "메트로놈"
        }.attach()

        // TabLayout의 indicator 색상 변경 리스너
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> binding.tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.colorForMusicSelect, null))
                    1 -> binding.tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.colorForMetronome, null))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        initEvent()
    }


    private fun initEvent() {
        persistentBottomSheetEvent()
    }

    private fun persistentBottomSheetEvent() {
        behavior = BottomSheetBehavior.from(binding.persistentBottomSheet)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d(TAG, "onStateChanged: 드래그 중")
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Log.d(TAG, "onStateChanged: 접음")
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        Log.d(TAG, "onStateChanged: 드래그")
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Log.d(TAG, "onStateChanged: 펼침")
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.d(TAG, "onStateChanged: 숨기기")
                    }

                    BottomSheetBehavior.STATE_SETTLING -> {
                        Log.d(TAG, "onStateChanged: 고정됨")
                    }
                }
            }
        })
    }

    companion object {
        private const val TAG = "PersistentFragment"
    }
}
