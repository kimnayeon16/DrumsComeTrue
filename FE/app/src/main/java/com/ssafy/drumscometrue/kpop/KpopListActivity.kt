package com.ssafy.drumscometrue.kpop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.adapter.kPopAdapter

class KpopListActivity : AppCompatActivity() {

    var kpopList = arrayListOf<Kpop>(
        Kpop("곰 세마리", "북극곰", "img_three_bears"),
        Kpop("나비야", "나비", "img_butterfly"),
        Kpop("Rooftop(옥탑방)", "N.Flying(엔플라잉)", "img_rooftop"),
        Kpop("너의 의미", "IU(아이유)", "img_meaningofyou"),
        Kpop("서울밤", "프롬(Fromm)", "img_seoul_night")
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kpop_list)

        val mRecyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        val mAdapter = kPopAdapter(this, kpopList){ kpop ->
            val intent = Intent(this, KpopPlayActivity::class.java)
            startActivity(intent)
            finish()
        }
        mRecyclerView.adapter = mAdapter
    }

}