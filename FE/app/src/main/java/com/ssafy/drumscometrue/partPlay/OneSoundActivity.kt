package com.ssafy.drumscometrue.partPlay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.databinding.ActivityOneSoundBinding
import com.ssafy.drumscometrue.databinding.ActivityPadPlayBinding
import com.ssafy.drumscometrue.freePlay.fragment.CameraFragment
import com.ssafy.drumscometrue.freePlay.fragment.PermissionsFragment
import com.ssafy.drumscometrue.partPlay.fragment.HihatFragment
import com.ssafy.drumscometrue.partPlay.fragment.OneSoundFragment
import com.ssafy.drumscometrue.partPlay.fragment.SnareFragment

class OneSoundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_sound)

        val receivedData = intent.getStringExtra("num").toString()

        val fragment: Fragment = if (receivedData == "1") {
            SnareFragment()
        } else if(receivedData == "2") {
            HihatFragment()
        } else if(receivedData == "3"){
            OneSoundFragment()  //Bass임다
        } else{
            CameraFragment()
        }

        // 프래그먼트를 추가 또는 교체
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        finish()
    }
}
