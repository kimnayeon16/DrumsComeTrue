package com.ssafy.drumscometrue.partPlay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.databinding.ActivityOneSoundBinding
import com.ssafy.drumscometrue.databinding.ActivityPadPlayBinding
import com.ssafy.drumscometrue.freePlay.fragment.PermissionsFragment
import com.ssafy.drumscometrue.partPlay.fragment.OneSoundFragment

class OneSoundActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityOneSoundBinding
    private lateinit var receivedData:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receivedData = intent.getStringExtra("num").toString()

        val bundle = Bundle()
        bundle.putString("receivedData", receivedData)
        val fragment = OneSoundFragment()
        fragment.arguments = bundle

        activityMainBinding = ActivityOneSoundBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
    }

    override fun onBackPressed() {
        finish()
    }
}
