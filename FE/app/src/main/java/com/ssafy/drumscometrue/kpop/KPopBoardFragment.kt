package com.ssafy.drumscometrue.kpop

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.SharedViewModel
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

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private var aa : TextView ?= null

    private lateinit var rootView: View
    private lateinit var hihatView: View


    //                val xPositionStart = dpToPx(rootView.context, 125f)
//                val xPositionEnd = dpToPx(rootView.context, -25f)
//                val dp35 = dpToPx(rootView.context, 35f)
    private val xPositionStart = 125f
    private val xPositionEnd = -25f
    private val dp40 = 40f
//    private val sharedViewModel1 = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    private var totalHit = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_kpop_board, container, false)
        hihatView = inflater.inflate(R.layout.fragment_drum_hi_hat, container, false)

        val score = arguments?.getString("score")
        val song = arguments?.getString("song")
        prelude = arguments?.getLong("prelude") ?: 0L
        interval = arguments?.getLong("interval") ?: 0L
        val songTextView = rootView?.findViewById<TextView>(R.id.songName)

        if(song != null){
            songTextView?.text = song
            aa = songTextView
        }

        if (score != null) {
            val assetManager = resources.assets //asset 폴더에 접근하기 위해
            val inputStream= assetManager.open("${score}.json") //악보json파일 열기
            //InputStream을 문자열로 변환하여 jsonString 변수에 저장, 이렇게 하면 JSON 파일의 내용을 문자열로 읽어올 수 있음.
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            //JSON 문자열을 파싱하여 JSONObject로 변환
            val jObject = JSONObject(jsonString)
            // "score"라는 키를 가진 JSON 배열을 가져오기
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

            //전주 시간 + 자세 잡는 시간 카운팅(12000)초 뒤에 scheduleNextFragment() 실행
            if (scoreList.isNotEmpty()) {
                handler.postDelayed({
                    scheduleNextFragment()
                }, prelude+10500)
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

            // 간격 시간마다 호출
            handler.postDelayed({
                updateDrumFragment(timeInSeconds, drumValuesArray)
                currentIndex++
                scheduleNextFragment()
            }, interval)
        }else{
            sharedViewModel.totalHit = totalHit
        }
    }

    companion object {
        fun dpToPx(context: Context, dp: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
        }
    }

    private fun updateDrumFragment(timeInSeconds: Double , drumValuesArray: JSONArray) {
        if(isAdded){
            var transaction = childFragmentManager.beginTransaction()

            //어떤 드럼을 쳤는지
            var dataFromCameraFragment = sharedViewModel.data
            var dataFromCameraFragment1 = sharedViewModel.data1
            var dataFromCameraFragment2 = sharedViewModel.data2
            var dataFromCameraFragment3 = sharedViewModel.data3
            var dataFromCameraFragment4 = sharedViewModel.data4
            var dataFromCameraFragment5 = sharedViewModel.data5
            var dataFromCameraFragment6 = sharedViewModel.data6
            var dataFromCameraFragment7 = sharedViewModel.data7
            var dataFromCameraFragment8 = sharedViewModel.data8
            var dataFromCameraFragment9 = sharedViewModel.data9
            System.out.println("!!!!! 어떤 드럼을 쳤나요? $dataFromCameraFragment")

            //하이햇 - 곰세마리, 나비야, 거미
            if (drumValuesArray.toString() == "[3]") {
                val drumHiHatFragment = DrumHiHatFragment(0)
                transaction.setCustomAnimations(
                    R.anim.enter_from_right, // 오른쪽에서 왼쪽으로 들어오는 애니메이션
                    0 // 왼쪽으로 나가는 애니메이션
                )
                transaction.replace(R.id.drumContainer, drumHiHatFragment)

                // ValueAnimator 초기화
                val animator = ValueAnimator.ofFloat(xPositionStart, xPositionEnd)
                animator.duration = 3000 // 애니메이션 지속 시간 (밀리초)

                animator.addUpdateListener { animation ->
                    val xPosition = animation.animatedValue as Float // 현재 애니메이션 중인 x 좌표
//                    System.out.println("X Position: $xPosition")
                    // X 좌표가 40dp +- 1일 때의 처리
                    if (xPosition >= dp40 - 1 && xPosition <= dp40 + 1) {
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3에서 발생")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            //hit 표시
                            hit?.visibility = View.VISIBLE
//                            val drumHiHatFragment = DrumHiHatFragment(1)
//                            transaction.replace(R.id.drumContainer, drumHiHatFragment)
                            //0.2초 뒤 사라지게 하고 값도 제거
                            handler.postDelayed({
                                hit?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                dataFromCameraFragment = ""
                            }, 100)
                        }
                    }
                }
                animator.start()

                //하이햇1 - 곰세마리, 나비야, 거미
            } else if (drumValuesArray.toString() == "[3.1]") {
                val drumHiHatFragment1 = DrumHiHatFragment1()
                transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    0
                )
                transaction.replace(R.id.drumContainer, drumHiHatFragment1)

                // ValueAnimator 초기화
                val animator = ValueAnimator.ofFloat(xPositionStart, xPositionEnd)
                animator.duration = 3000 // 애니메이션 지속 시간 (밀리초)

                animator.addUpdateListener { animation ->
                    val xPosition = animation.animatedValue as Float // 현재 애니메이션 중인 x 좌표
//                    System.out.println("X Position: $xPosition")

                    // X 좌표가 40dp +- 1일 때의 처리
                    if (xPosition >= dp40 - 1 && xPosition <= dp40 + 1) {
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3.1에서 발생")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            //hit 표시
                            hit?.visibility = View.VISIBLE
//                            val drumHiHatFragment = DrumHiHatFragment(1)
//                            transaction.replace(R.id.drumContainer, drumHiHatFragment)
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                dataFromCameraFragment = ""
                            }, 100)
                        }
                    }
                }
                animator.start()

                //하이햇2 - 곰세마리
            }else if (drumValuesArray.toString() == "[3.2]") {
                val drumHiHatFragment2 = DrumHiHatFragment2()
                transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    0
                )
                transaction.replace(R.id.drumContainer, drumHiHatFragment2)

                // ValueAnimator 초기화
                val animator = ValueAnimator.ofFloat(xPositionStart, xPositionEnd)
                animator.duration = 3000 // 애니메이션 지속 시간 (밀리초)

                animator.addUpdateListener { animation ->
                    val xPosition = animation.animatedValue as Float // 현재 애니메이션 중인 x 좌표
                    System.out.println("X Position: $xPosition")

                    // X 좌표가 40dp +- 1일 때의 처리
                    if (xPosition >= dp40 - 1 && xPosition <= dp40 + 1) {
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3.2에서 발생")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            //hit 표시
                            hit?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                dataFromCameraFragment = ""
                            }, 100)
                        }
                    }
                }
                animator.start()

                //하이햇, 스네어 - 곰세마리, 나비야, 거미
            }else if (drumValuesArray.toString() == "[3,8]") {
                val drumHiHatFragment = DrumHiHatFragment(0)
                val drumSnareFragment = DrumSnareFragment()

                transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    0
                )
                transaction.add(R.id.drumContainer, drumHiHatFragment)
                transaction.add(R.id.drumContainer, drumSnareFragment)

                // ValueAnimator 초기화
                val animator = ValueAnimator.ofFloat(xPositionStart, xPositionEnd)
                animator.duration = 3000 // 애니메이션 지속 시간 (밀리초)

                animator.addUpdateListener { animation ->
                    val xPosition = animation.animatedValue as Float // 현재 애니메이션 중인 x 좌표
//                    System.out.println("X Position: $xPosition")

                    // X 좌표가 40dp +- 1일 때의 처리
                    if (xPosition >= dp40 - 1 && xPosition <= dp40 + 1) {
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3,8에서 발생")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            //hit 표시
                            hit?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                dataFromCameraFragment = ""
                            }, 100)
                        }
                        if(dataFromCameraFragment1 == "snare"){
                            totalHit+=1
                            var snare = rootView?.findViewById<TextView>(R.id.hitSnare)
                            //hit 표시
                            snare?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                snare?.visibility = View.INVISIBLE
                                sharedViewModel.data1 = ""
                                dataFromCameraFragment1 = ""
                            }, 100)
                        }
                    }
                }
                animator.start()


                //하이햇, 베이스 - 나비야, 거미
            } else if (drumValuesArray.toString() == "[3,10]") {
                val drumHiHatFragment = DrumHiHatFragment(0)
                val drumBassFragment = DrumBassFragment()

                transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    0
                )
                transaction.add(R.id.drumContainer, drumHiHatFragment)
                transaction.add(R.id.drumContainer, drumBassFragment)

                // ValueAnimator 초기화
                val animator = ValueAnimator.ofFloat(xPositionStart, xPositionEnd)
                animator.duration = 3000 // 애니메이션 지속 시간 (밀리초)

                animator.addUpdateListener { animation ->
                    val xPosition = animation.animatedValue as Float // 현재 애니메이션 중인 x 좌표
                    System.out.println("X Position: $xPosition")

                    // X 좌표가 40dp +- 1일 때의 처리
                    if (xPosition >= dp40 - 1 && xPosition <= dp40 + 1) {
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"){
                            totalHit+=1
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            //hit 표시
                            hit?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                dataFromCameraFragment = ""
                            }, 100)
                        }
                        if(dataFromCameraFragment9 == "bass"){
                            totalHit+=1
                            var bass = rootView?.findViewById<TextView>(R.id.hitBass)
                            //hit 표시
                            bass?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                bass?.visibility = View.INVISIBLE
                                sharedViewModel.data9 = ""
                                dataFromCameraFragment9 = ""
                            }, 100)
                        }
                    }
                }
                animator.start()
                //크래쉬, 베이스 - 거미
            }else if (drumValuesArray.toString() == "[1,10]") {
                val drumCrashFragment = DrumCrashFragment()
                val drumBassFragment = DrumBassFragment()

                transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    0
                )
                transaction.add(R.id.drumContainer, drumCrashFragment)
                transaction.add(R.id.drumContainer, drumBassFragment)

                // ValueAnimator 초기화
                val animator = ValueAnimator.ofFloat(xPositionStart, xPositionEnd)
                animator.duration = 3000 // 애니메이션 지속 시간 (밀리초)

                animator.addUpdateListener { animation ->
                    val xPosition = animation.animatedValue as Float // 현재 애니메이션 중인 x 좌표
                    System.out.println("X Position: $xPosition")

                    // X 좌표가 40dp +- 1일 때의 처리
                    if (xPosition >= dp40 - 1 && xPosition <= dp40 + 1) {
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment2 == "crash"){
                            totalHit+=1
                            var crash = rootView?.findViewById<TextView>(R.id.hitCrash)
                            //hit 표시
                            crash?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                crash?.visibility = View.INVISIBLE
                                sharedViewModel.data2 = ""
                                dataFromCameraFragment2 = ""
                            }, 100)
                        }
                        if(dataFromCameraFragment9 == "bass"){
                            totalHit+=1
                            var bass = rootView?.findViewById<TextView>(R.id.hitBass)
                            //hit 표시
                            bass?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                bass?.visibility = View.INVISIBLE
                                sharedViewModel.data9 = ""
                                dataFromCameraFragment9 = ""
                            }, 100)
                        }
                    }
                }
                animator.start()
                //크래쉬, 스네어 - 거미
            }else if (drumValuesArray.toString() == "[1,8]") {
                val drumCrashFragment = DrumCrashFragment()
                val drumhiHatFragment = DrumHiHatFragment(0)

                transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    0
                )
                transaction.add(R.id.drumContainer, drumCrashFragment)
                transaction.add(R.id.drumContainer, drumhiHatFragment)

                // ValueAnimator 초기화
                val animator = ValueAnimator.ofFloat(xPositionStart, xPositionEnd)
                animator.duration = 3000 // 애니메이션 지속 시간 (밀리초)

                animator.addUpdateListener { animation ->
                    val xPosition = animation.animatedValue as Float // 현재 애니메이션 중인 x 좌표
                    System.out.println("X Position: $xPosition")

                    // X 좌표가 40dp +- 1일 때의 처리
                    if (xPosition >= dp40 - 1 && xPosition <= dp40 + 1) {
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment2 == "crash"){
                            totalHit+=1
                            var crash = rootView?.findViewById<TextView>(R.id.hitCrash)
                            //hit 표시
                            crash?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                crash?.visibility = View.INVISIBLE
                                sharedViewModel.data2 = ""
                                dataFromCameraFragment2 = ""
                            }, 100)
                        }
                        if(dataFromCameraFragment == "openHiHat"){
                            totalHit+=1
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            //hit 표시
                            hit?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                dataFromCameraFragment = ""
                            }, 100)
                        }
                    }
                }
                animator.start()
            }
            transaction.commit()
        }
    }
}