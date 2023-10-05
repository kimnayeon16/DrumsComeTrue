package com.ssafy.drumscometrue.kpop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.adapter.kPopAdapter

class KpopListActivity : AppCompatActivity() {

    private var kpopList = arrayListOf<Kpop>(
        Kpop("작은별", "작은별", "img_seoul_night", "Star", 5610, 683, 48000, "레벨1"),
        Kpop("곰 세마리", "북극곰", "img_three_bears", "ThreeBears", 6590, 475, 38000, "레벨2"),
        Kpop("나비야", "나비", "img_butterfly", "Butterfly", 6517, 483, 42000, "레벨2"),
        Kpop("거미가 줄을 타고 올라갑니다", "거미", "img_spider", "Spider", 4455, 383, 46000, "레벨3")
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kpop_list)

        val mRecyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        mRecyclerView.layoutManager = LinearLayoutManager(this)



        val mAdapter = kPopAdapter(this, kpopList){ kpop ->
            val intent = Intent(this, KpopPlayActivity::class.java)
            intent.putExtra("song", kpop.song)
            intent.putExtra("artist", kpop.singer)
            intent.putExtra("score", kpop.score)
            intent.putExtra("prelude", kpop.prelude)
            intent.putExtra("interval", kpop.interval)
            intent.putExtra("songTotalTime", kpop.songTotalTime)
            intent.putExtra("level", kpop.level)
            startActivity(intent)
//            finish()
        }
        mRecyclerView.adapter = mAdapter

    }
}