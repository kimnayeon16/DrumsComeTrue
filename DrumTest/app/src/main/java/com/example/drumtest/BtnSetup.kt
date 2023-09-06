package com.example.drumtest

import android.app.Activity
import android.widget.Button


class BtnSetup(activity: Activity, private val drumSoundPlayer: DrumSoundPlayer) {

    // 베이스
    private val bass: Button = activity.findViewById(R.id.bass)

    // 스네어
    private val snare: Button = activity.findViewById(R.id.snare)

    // 심벌
    private val openHiHat: Button = activity.findViewById(R.id.open_hi_hat_cymbal)
    private val closeHiHat: Button = activity.findViewById(R.id.close_hi_hat_cymbal)
    private val pedalHiHat: Button = activity.findViewById(R.id.pedal_hi_hat_cymbal)
    private val crash: Button = activity.findViewById(R.id.crash_cymbal)
    private val ride: Button = activity.findViewById(R.id.ride_cymbal)

    // 탐탐
    private val highTom: Button = activity.findViewById(R.id.high_tom)
    private val midTom: Button = activity.findViewById(R.id.mid_tom)
    private val floorTom: Button = activity.findViewById(R.id.floor_tom)

    init {
        setupButtons()
    }

    private fun setupButtons() {
        bass.setOnClickListener {
            drumSoundPlayer.playSoundFromAssets("bass.wav")
        }

        snare.setOnClickListener {
            drumSoundPlayer.playSoundFromAssets("snare.wav")
        }

        openHiHat.setOnClickListener {
            drumSoundPlayer.playSoundFromAssets("open_hat.wav")
        }

        closeHiHat.setOnClickListener {
            drumSoundPlayer.playSoundFromAssets("closed_hat.wav")
        }

        pedalHiHat.setOnClickListener {
            drumSoundPlayer.playSoundFromAssets("pedal_hat.wav")
        }

        crash.setOnClickListener {
            drumSoundPlayer.playSoundFromAssets("crash.wav")
        }

        ride.setOnClickListener {
            drumSoundPlayer.playSoundFromAssets("ride.wav")
        }

        highTom.setOnClickListener {
            drumSoundPlayer.playSoundFromAssets("high_tom.wav")
        }

        midTom.setOnClickListener {
            drumSoundPlayer.playSoundFromAssets("mid_tom.wav")
        }

        floorTom.setOnClickListener {
            drumSoundPlayer.playSoundFromAssets("floor_tom.wav")
        }
    }

}
