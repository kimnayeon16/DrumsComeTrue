package com.ssafy.drumscometrue.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ssafy.drumscometrue.persistentbottom.MusicFragment
import com.ssafy.drumscometrue.persistentbottom.PersistentFragment
import com.ssafy.drumscometrue.tutorial.MetronomeFragment
import com.ssafy.drumscometrue.tutorial.RecordFragment

class SlidePagerAdapter(fragment: PersistentFragment) : FragmentStateAdapter(fragment.childFragmentManager, fragment.lifecycle) {

    override fun getItemCount(): Int {
        Log.d("SlidePagerAdapter", "getItemCount called: 2")
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        Log.d("SlidePagerAdapter", "createFragment for position: $position")
        return when (position) {
            0 -> {
                Log.d("SlidePagerAdapter", "Creating SelectFragment")
                MusicFragment()
            }
            1 -> {
                Log.d("SlidePagerAdapter", "Creating RecordFragment")
                RecordFragment()
            }
            2 -> {
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
