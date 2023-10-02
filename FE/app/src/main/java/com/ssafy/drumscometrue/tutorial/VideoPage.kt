package com.ssafy.drumscometrue.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.freePlay.FreePlayActivity

class VideoPage : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video_page, container, false)

        view.findViewById<Button>(R.id.tutor_btn).setOnClickListener {
            val intent = Intent(requireActivity(), FreePlayActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}
