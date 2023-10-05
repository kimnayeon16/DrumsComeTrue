package com.ssafy.drumscometrue.kpop

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import com.ssafy.drumscometrue.R

class DrumHiHatFragment() : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_drum_hi_hat, container, false)

        val imageView = rootView.findViewById<ImageView>(R.id.hiHat)

//        if (isVISIBLE == 1) {
//            imageView.visibility = View.INVISIBLE
//            imageView.invalidate()
//            System.out.println("언제 invisible로 바꾸는건데?")
//        }else{
//            imageView.visibility = View.VISIBLE
//            imageView.invalidate()
//            System.out.println("다시 보여줘")
//        }

        return rootView
    }
}