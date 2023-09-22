package com.ssafy.drumscometrue.user.findid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.user.findid.fragment.FindIdMainFragment

class FindIdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_id)

        // 초기 프레그먼트 설정
        if (savedInstanceState == null) {
            replaceFragment(FindIdMainFragment())
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.find_id_ui_fragment, fragment)
        transaction.commit()
    }

}
