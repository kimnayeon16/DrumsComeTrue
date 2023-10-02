package com.ssafy.drumscometrue.tutorial

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.alertdialog.DialogUtils

class MetronomeFragment : Fragment() {
    private lateinit var startPauseButton: ImageButton
    private lateinit var bpmSeekBar: SeekBar
    private lateinit var bpmText: TextView
    private lateinit var soundTextView: TextView

    private var soundPool: SoundPool? = null
    private val soundIds = HashMap<String, Int>()
    private var selectedSoundIndex = 0
    private var isMetronomePlaying = false

    private val handler = Handler(Looper.getMainLooper())
    private var metronomeRunnable: Runnable? = null

    private var currentBpm = 60
    private val tickDuration: Long
        get() = (60 * 1000 / currentBpm).toLong()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_metronome, container, false)

        startPauseButton = view.findViewById(R.id.startPauseButton)
        bpmSeekBar = view.findViewById(R.id.bpmSeekBar)
        bpmText = view.findViewById(R.id.bpmText)
        bpmText.text = getString(R.string.bpm_value, currentBpm)
        soundTextView = view.findViewById(R.id.soundTextView)

        initSoundPool()

        startPauseButton.setOnClickListener { toggleMetronome() }

        bpmSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentBpm = progress
                bpmText.text = getString(R.string.bpm_value, currentBpm)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not used
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Not used
            }
        })

        val changeSoundButton = view.findViewById<ImageButton>(R.id.soundChangeBtn)
        changeSoundButton.setOnClickListener { changeSound() }

        val plusButton = view.findViewById<ImageButton>(R.id.plusButton)
        plusButton.setOnClickListener {
            if (currentBpm < 240) {
                currentBpm++
                bpmSeekBar.progress = currentBpm
                bpmText.text = getString(R.string.bpm_value, currentBpm)
            }
        }

        val minusButton = view.findViewById<ImageButton>(R.id.minusButton)
        minusButton.setOnClickListener {
            if (currentBpm > 1) {
                currentBpm--
                bpmSeekBar.progress = currentBpm
                bpmText.text = getString(R.string.bpm_value, currentBpm)
            }
        }

        val metronomeInfoButton = view.findViewById<ImageButton>(R.id.metronomeInfoBtn)
        metronomeInfoButton.setOnClickListener {
            val title = "메트로놈 사용법"
            val message = "\uD83D\uDD01 - 소리 변경     ▶️ - 재생     +- - bpm 1씩 증가/감소"
            DialogUtils.showInfoDialog(requireContext(), title, message)
        }
        return view
    }

    private fun initSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        val soundNames = arrayOf("beep", "click", "ding", "wood")

        for (soundName in soundNames) {
            val resourceId = resources.getIdentifier("metronome_$soundName", "raw", requireActivity().packageName)
            soundIds[soundName] = soundPool?.load(requireActivity(), resourceId, 1) ?: 0
        }
    }

    private fun toggleMetronome() {
        if (isMetronomePlaying) {
            stopMetronome()
        } else {
            startMetronome()
        }
    }

    private fun startMetronome() {
        startPauseButton.setImageResource(R.drawable.stop_btn)
        isMetronomePlaying = true

        metronomeRunnable = object : Runnable {
            override fun run() {
                val soundName = soundIds.keys.elementAt(selectedSoundIndex)
                val soundId = soundIds[soundName]
                soundPool?.play(soundId!!, 1.0f, 1.0f, 0, 0, 1.0f)
                handler.postDelayed(this, tickDuration)
            }
        }

        handler.post(metronomeRunnable!!)
    }

    private fun stopMetronome() {
        startPauseButton.setImageResource(R.drawable.play_arrow)
        isMetronomePlaying = false

        handler.removeCallbacks(metronomeRunnable!!)
    }

    private fun changeSound() {
        selectedSoundIndex = (selectedSoundIndex + 1) % soundIds.size
        val soundName = soundIds.keys.elementAt(selectedSoundIndex)
        soundTextView.text = soundName
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool?.release()
        soundPool = null
    }
}