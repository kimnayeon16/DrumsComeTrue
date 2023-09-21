package com.ssafy.drumscometrue.kpop

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.ssafy.drumscometrue.R

class KpopListActivity : AppCompatActivity() {

    private var kpopList: ArrayList<KpopList> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kpop_list)

        kpopList = Constants.getKpopSongs()




        var btnStart :Button =findViewById(R.id.btn_play)

        btnStart.setOnClickListener{
            val intent = Intent(this, KpopPlayActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}