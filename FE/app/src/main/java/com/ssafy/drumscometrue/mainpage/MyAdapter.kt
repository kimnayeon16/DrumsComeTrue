package com.android.example.deletepjt

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyAdapter(
    fragmentActivity: FragmentActivity,
    private val myModelArrayList: ArrayList<MyModel>,
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return myModelArrayList.size
    }

    override fun createFragment(position: Int): Fragment {
        // Create a new Fragment based on the position
        return when (position) {
            0 -> PageFreePlayingFragment()
            1 -> PageTutorFragment()
            2 -> PagePracticeFragment()
            3 -> PageLaunchFragment()
            // Add more cases for other positions
            else -> PageMainFragment() // Default Fragment
        }
    }
}