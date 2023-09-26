package com.ssafy.drumscometrue.kpop

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.drumscometrue.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Timer
import java.util.TimerTask

class KPopBoardFragment : Fragment() {
    private val handler = Handler()
    private var currentIndex = 0 // JSON 배열의 인덱스
    private val scoreList = mutableListOf<Pair<Double, JSONArray>>()
    private var prelude : Long = 0
    private var interval : Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_kpop_board, container, false)

        val score = arguments?.getString("score")
        val song = arguments?.getString("song")
        prelude = arguments?.getLong("prelude") ?: 0L
        interval = arguments?.getLong("interval") ?: 0L
//        System.out.println("ppppppprrrrrrrrrrrreeeeeeeeeeeeeelllllllllllluuuuuuuuuudddddddddddeeeeeeee $prelude")
//        System.out.println("iiiiiiiiinnnnnnnnnntttttttttteeeeeerrrrrrrrvvvvvvvvvvvvaaaaaaalllllll $interval")
        val songTextView = rootView?.findViewById<TextView>(R.id.songName)

        if(song != null){
            songTextView?.text = song
        }

        if (score != null) {
            val assetManager = resources.assets
            val inputStream= assetManager.open("${score}.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val jObject = JSONObject(jsonString)
            val jArray = jObject.getJSONArray("score")
            // 필요한 악보 정보 추출
            for (i in 0 until jArray.length()) {
                val jsonObject: JSONObject = jArray.getJSONObject(i)
                val time = jsonObject.getString("time")
                val drumValuesArray = jsonObject.getJSONArray("drum")

                scoreList.add(Pair(time.toDouble(), drumValuesArray))
                System.out.println("!!!!!!!!!! ${time} 초에 ${drumValuesArray} 쳐!!!!")
            }

            System.out.println(scoreList)
            if (scoreList.isNotEmpty()) {
                handler.postDelayed({
                    scheduleNextFragment()
                }, prelude)
            }
        }
        return rootView
    }

    private fun scheduleNextFragment() {
        if (currentIndex < scoreList.size) {
            val pair = scoreList[currentIndex]
            val timeInSeconds = pair.first
            val drumValuesArray = pair.second
            System.out.println("TimeInSeconed는 ${timeInSeconds}이고, drumValuesArray는 ${drumValuesArray}")

            // 예약된 시간(밀리초)을 계산합니다.
            val timeInMillis = (timeInSeconds * 1000).toLong()

            handler.postDelayed({
                updateDrumFragment(drumValuesArray)
                currentIndex++
                scheduleNextFragment()
            }, interval)
        }
    }

    private fun updateDrumFragment(drumValuesArray: JSONArray) {
        //곰세마리, 나비야, 거미
        if (drumValuesArray.toString() == "[3]") {
            val drumHiHatFragment = DrumHiHatFragment()
            val transaction = childFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.enter_from_right, // 오른쪽에서 왼쪽으로 들어오는 애니메이션
                0// 왼쪽으로 나가는 애니메이션
            )
            transaction.replace(R.id.drumContainer, drumHiHatFragment)
            transaction.commit()
            //곰세마리, 나비야, 거미
        } else if (drumValuesArray.toString() == "[3.1]") {
            val drumHiHatFragment1 = DrumHiHatFragment1()
            val transaction = childFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.enter_from_right, // 오른쪽에서 왼쪽으로 들어오는 애니메이션
                0// 왼쪽으로 나가는 애니메이션
            )
            transaction.replace(R.id.drumContainer, drumHiHatFragment1)
            transaction.commit()
            //곰세마리
        }else if (drumValuesArray.toString() == "[3.2]") {
            val drumHiHatFragment2 = DrumHiHatFragment2()
            val transaction = childFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.enter_from_right, // 오른쪽에서 왼쪽으로 들어오는 애니메이션
                0// 왼쪽으로 나가는 애니메이션
            )
            transaction.replace(R.id.drumContainer, drumHiHatFragment2)
            transaction.commit()
            //곰세마리, 나비야, 거미
        }else if (drumValuesArray.toString() == "[3,8]") {
            val transaction = childFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.enter_from_right, // 오른쪽에서 왼쪽으로 들어오는 애니메이션
                0 // 왼쪽으로 나가는 애니메이션
            )
            val drumHiHatFragment = DrumHiHatFragment()
            transaction.add(R.id.drumContainer, drumHiHatFragment)

            val drumSnareFragment = DrumSnareFragment()
            transaction.add(R.id.drumContainer, drumSnareFragment)

            transaction.commit()
            //나비야, 거미
        }else if (drumValuesArray.toString() == "[3,10]") {
            val transaction = childFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.enter_from_right, // 오른쪽에서 왼쪽으로 들어오는 애니메이션
                0 // 왼쪽으로 나가는 애니메이션
            )
            val drumHiHatFragment = DrumHiHatFragment()
            transaction.add(R.id.drumContainer, drumHiHatFragment)

            val drumBassFragment = DrumBassFragment()
            transaction.add(R.id.drumContainer, drumBassFragment)

            transaction.commit()
            //거미
        }else if (drumValuesArray.toString() == "[1,10]") {
            val transaction = childFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.enter_from_right, // 오른쪽에서 왼쪽으로 들어오는 애니메이션
                0 // 왼쪽으로 나가는 애니메이션
            )
            val drumCrashFragment = DrumCrashFragment()
            transaction.add(R.id.drumContainer, drumCrashFragment)

            val drumBassFragment = DrumBassFragment()
            transaction.add(R.id.drumContainer, drumBassFragment)

            transaction.commit()
        }
    }
}