package com.ssafy.drumscometrue.partPlay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.databinding.ActivityPadPlayBinding
import com.ssafy.drumscometrue.persistentbottom.PersistentFragment

class PadPlayActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityPadPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityPadPlayBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        // PersistentFragment 추가
        if (savedInstanceState == null) { // 첫 생성 시만 Fragment 추가
            supportFragmentManager.beginTransaction()
                .replace(R.id.persistent_fragment_container, PersistentFragment())
                .commit()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}