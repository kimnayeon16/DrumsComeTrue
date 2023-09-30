package com.ssafy.drumscometrue.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ssafy.drumscometrue.persistentbottom.PersistentFragment
import com.ssafy.drumscometrue.practice.MetronomeFragment
import com.ssafy.drumscometrue.practice.RecordFragment

class SlidePagerAdapter(fragment: PersistentFragment) : FragmentStateAdapter(fragment.childFragmentManager, fragment.lifecycle) {

    override fun getItemCount(): Int {
        Log.d("SlidePagerAdapter", "getItemCount called: 2")
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        Log.d("SlidePagerAdapter", "createFragment for position: $position")
        return when (position) {
            0 -> {
                Log.d("SlidePagerAdapter", "Creating RecordFragment")
                RecordFragment()
            }
            1 -> {
                Log.d("SlidePagerAdapter", "Creating MetronomeFragment")
                MetronomeFragment()
            }
            else -> {
                Log.e("SlidePagerAdapter", "Invalid position: $position")
                throw IllegalStateException("Invalid position")
            }
        }
    }
}
