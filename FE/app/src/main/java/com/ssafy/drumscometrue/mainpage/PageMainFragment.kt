package com.android.example.deletepjt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.navigation.NavigationBarView

class PageMainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the NavigationBarView in the main activity's layout.
        val navigationBarView = requireActivity().findViewById<NavigationBarView>(R.id.nav_bottom)

        // Handle item selection in the NavigationBarView.
        navigationBarView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.containers, PageMainFragment())
                        .commit()
                    true
                }
                R.id.nav_freeplaying -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.containers, PageFreePlayingFragment())
                        .commit()
                    true
                }
                R.id.nav_mypage -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.containers, PageMyFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}