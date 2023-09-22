package com.ssafy.drumscometrue.mainpage

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterViewPager @JvmOverloads constructor(
    fragmentActivity: FragmentActivity,
    private val arr: ArrayList<Fragment>
) : FragmentStateAdapter(fragmentActivity) {

    @NonNull
    override fun createFragment(position: Int): Fragment {
        return arr[position]
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}