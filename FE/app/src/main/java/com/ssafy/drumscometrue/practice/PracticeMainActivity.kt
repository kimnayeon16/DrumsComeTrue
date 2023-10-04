package com.ssafy.drumscometrue.practice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.drumscometrue.MainActivity
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.freePlay.FreePlayActivity
import com.ssafy.drumscometrue.kpop.KpopListActivity
import com.ssafy.drumscometrue.mainpage.UserPageActivity
import com.ssafy.drumscometrue.padPlay.PadPlayActivity

class PracticeMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_main)
        val practice1Layout = findViewById<ConstraintLayout>(R.id.practice1_select)
        val practice2Layout = findViewById<ConstraintLayout>(R.id.practice_select2)

        val navBottom = findViewById<BottomNavigationView>(R.id.nav_bottom)
        navBottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.to_practice -> {
                    val intent = Intent(this, KpopListActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.to_snare -> {
                    val intent = Intent(this, PadPlayActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        practice1Layout.setOnClickListener {
            val intent = Intent(this, KpopListActivity::class.java)
            startActivity(intent)
        }

        practice2Layout.setOnClickListener {
            val intent = Intent(this, PadPlayActivity::class.java)
            startActivity(intent)
        }
    }

}