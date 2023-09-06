package com.example.drumtest

import android.content.Context
import android.media.SoundPool

class DrumSoundPlayer(context: Context) {
    private val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(10).build()
    private val soundMap: HashMap<String, Int> = HashMap()

    init {
        // 사운드 파일 미리 로드
        soundMap["bass.wav"] = soundPool.load(context, R.raw.bass, 1)
        soundMap["snare.wav"] = soundPool.load(context, R.raw.snare, 1)
        soundMap["open_hat.wav"] = soundPool.load(context, R.raw.open_hat, 1)
        soundMap["closed_hat.wav"] = soundPool.load(context, R.raw.closed_hat, 1)
        soundMap["pedal_hat.wav"] = soundPool.load(context, R.raw.pedal_hat, 1)
        soundMap["crash.wav"] = soundPool.load(context, R.raw.crash, 1)
        soundMap["ride.wav"] = soundPool.load(context, R.raw.ride, 1)
        soundMap["high_tom.wav"] = soundPool.load(context, R.raw.high_tom, 1)
        soundMap["mid_tom.wav"] = soundPool.load(context, R.raw.mid_tom, 1)
        soundMap["floor_tom.wav"] = soundPool.load(context, R.raw.floor_tom, 1)
    }

    fun playSoundFromAssets(soundFileName: String) {
        soundMap[soundFileName]?.let { soundID ->
            soundPool.play(soundID, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun release() {
        soundPool.release()
    }
}
