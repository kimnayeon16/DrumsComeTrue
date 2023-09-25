package com.ssafy.drumscometrue.kpop

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ssafy.drumscometrue.R
import java.util.Timer
import kotlin.concurrent.timer

class KPopCountFragment : Fragment() {
    private var startTime = 5
    private var timerTask: Timer?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_kpop_count, container, false)

        var countTime = rootView?.findViewById<TextView>(R.id.countTime)
        startTime = startTime.toString().toInt()*100
        timerTask = timer(period = 10){
            val sec = startTime / 100
            activity?.runOnUiThread {
                if (sec > 1) {
                    countTime?.text = "${sec-1}"
                } else if (sec == 1){
                    countTime?.text = "Start!"
                } else{
                    countTime?.text = ""
                    countTime?.setPadding(0, 0, 0, 0)

                    timerTask?.cancel()
                }
            }
            startTime--
        }
        return rootView
    }
}