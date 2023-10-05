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
    private var correctHit = 0
//    private val sharedViewModel1 = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    private var totalHit = 0

    private var margin = 0

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
//        val songTextView = rootView?.findViewById<TextView>(R.id.songName)

//        if(song != null){
//            songTextView?.text = song
//            aa = songTextView
//        }

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
//                System.out.println("!!!!!!!!!! ${time} 초에 ${drumValuesArray} 쳐!!!!")
            }

            handler.postDelayed({
                var poseGuide = rootView.findViewById<TextView>(R.id.poseGuide)
                poseGuide.visibility = View.INVISIBLE
            }, 13000)

            //전주 시간 + 자세 잡는 시간 카운팅(12000)초 뒤에 scheduleNextFragment() 실행
            if (scoreList.isNotEmpty()) {
                handler.postDelayed({
                    scheduleNextFragment()
                }, prelude+10500)
            }
        }

        // 비율을 계산합니다.
        val ratio = 0.10f // 10% 비율

        // 마진을 계산하고 설정합니다.
        margin = (resources.displayMetrics.widthPixels * ratio).toInt()
//        dp40 = margin
        correctHit = 17

        val finishLine = rootView.findViewById<TextView>(R.id.finishLine)
        val layoutParams = finishLine.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginStart = margin
        finishLine.layoutParams = layoutParams

        return rootView

    }


    private fun scheduleNextFragment() {
        if (currentIndex < scoreList.size) {
            val pair = scoreList[currentIndex]
            val timeInSeconds = pair.first
            val drumValuesArray = pair.second

            // 예약된 시간(밀리초)을 계산합니다.
            val timeInMillis = (timeInSeconds * 1000).toLong()

            // 간격 시간마다 호출
            handler.postDelayed({
                updateDrumFragment(timeInSeconds, drumValuesArray)
                currentIndex++
                scheduleNextFragment()
            }, interval)
        }else{
            handler.postDelayed({
                System.out.println("totalHit ???!!!!!! $totalHit")
                sharedViewModel.totalHit = totalHit
            }, 2000)
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

            var boolean = 0

            //어떤 드럼을 쳤는지
            var dataFromCameraFragment = sharedViewModel.data //오픈하이햇
            var dataFromCameraFragment1 = sharedViewModel.data1 //스네어
            var dataFromCameraFragment2 = sharedViewModel.data2 //크래쉬
            var dataFromCameraFragment3 = sharedViewModel.data3 // 라이드
            var dataFromCameraFragment4 = sharedViewModel.data4 //클로즈하이햇
            var dataFromCameraFragment5 = sharedViewModel.data5 //
            var dataFromCameraFragment6 = sharedViewModel.data6 //하이탐
            var dataFromCameraFragment7 = sharedViewModel.data7 //미들탐
            var dataFromCameraFragment8 = sharedViewModel.data8 //플로우
            var dataFromCameraFragment9 = sharedViewModel.data9 //베이스

            correctHit = 14

            //하이햇 - 곰세마리, 나비야, 거미
            if (drumValuesArray.toString() == "[3]") {
                val drumHiHatFragment = DrumHiHatFragment()
                transaction.setCustomAnimations(
                    R.anim.enter_from_right, // 오른쪽에서 왼쪽으로 들어오는 애니메이션
                    0 // 왼쪽으로 나가는 애니메이션
                )
                transaction.replace(R.id.drumContainer, drumHiHatFragment)

                // ValueAnimator 초기화
                val animator = ValueAnimator.ofFloat(xPositionStart, xPositionEnd)
                animator.duration = 3000 // 애니메이션 지속 시간 (밀리초)
                animator.addUpdateListener { animation ->
                        val xPosition = animation.animatedValue as Float
                        // X 좌표가 40dp +- 1일 때의 처리
                        if (xPosition >= correctHit + 2 && xPosition <= correctHit + 3 && boolean == 0) {
                            boolean = 1
                            if (dataFromCameraFragment == "openHiHat" || dataFromCameraFragment4 == "closedHat"){
                                System.out.println("3에서 발생 xPosition : $xPosition")
                                totalHit += 1
                                System.out.println("$totalHit : 3에서 발생 총개수")
                                var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                                hit?.visibility = View.VISIBLE
                                var good = rootView?.findViewById<ImageView>(R.id.good)
                                good?.visibility = View.VISIBLE

                                handler.postDelayed({
                                    hit?.visibility = View.INVISIBLE
                                    good?.visibility = View.INVISIBLE
                                    sharedViewModel.data = ""
                                    sharedViewModel.data4 = ""
                                    dataFromCameraFragment = ""
                                    dataFromCameraFragment4 = ""
                                }, 200)
                            }
                        }else if (xPosition >= correctHit + 1 && xPosition <= correctHit + 2 && boolean == 0) {
                            boolean = 1
                            if (dataFromCameraFragment == "openHiHat" || dataFromCameraFragment4 == "closedHat"){
                                System.out.println("3에서 발생 xPosition : $xPosition")
                                totalHit += 1
                                System.out.println("$totalHit : 3에서 발생 총개수")
                                var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                                hit?.visibility = View.VISIBLE
                                var great = rootView?.findViewById<ImageView>(R.id.great)
                                great?.visibility = View.VISIBLE
                                handler.postDelayed({
                                    hit?.visibility = View.INVISIBLE
                                    great?.visibility = View.INVISIBLE
                                    sharedViewModel.data = ""
                                    sharedViewModel.data4 = ""
                                    dataFromCameraFragment = ""
                                    dataFromCameraFragment4 = ""
                                }, 200)
                            }
                        }else if (xPosition >= correctHit - 1 && xPosition <= correctHit + 1 && boolean == 0) {
                            boolean = 1
                            if (dataFromCameraFragment == "openHiHat" || dataFromCameraFragment4 == "closedHat"){
                                totalHit += 1
                                System.out.println("$totalHit : 3에서 발생 총개수")
                                var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                                hit?.visibility = View.VISIBLE
                                var perfect = rootView?.findViewById<ImageView>(R.id.perfect)
                                perfect?.visibility = View.VISIBLE

                                handler.postDelayed({
                                    hit?.visibility = View.INVISIBLE
                                    perfect?.visibility = View.INVISIBLE
                                    sharedViewModel.data = ""
                                    sharedViewModel.data4 = ""
                                    dataFromCameraFragment = ""
                                    dataFromCameraFragment4 = ""
                                }, 200)
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

                val animator = ValueAnimator.ofFloat(xPositionStart, xPositionEnd)
                animator.duration = 3000 // 애니메이션 지속 시간 (밀리초)

                animator.addUpdateListener { animation ->
                    val xPosition = animation.animatedValue as Float // 현재 애니메이션 중인 x 좌표

                    // X 좌표가 40dp +- 1일 때의 처리
                    if (xPosition >= correctHit + 2 && xPosition <= correctHit + 3 && boolean == 0) {
                        boolean = 1
                        System.out.println("3.1에서 발생 xPosition : $xPosition")
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3.1에서 발생 총개수")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var good = rootView?.findViewById<ImageView>(R.id.good)
                            good?.visibility = View.VISIBLE
                            val drumHiHatFragment = DrumHiHatFragment()
                            transaction.replace(R.id.drumContainer, drumHiHatFragment)
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                good?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit + 1 && xPosition <= correctHit + 2 && boolean == 0) {
                        boolean = 1
                        System.out.println("3.1에서 발생 xPosition : $xPosition")
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3.1에서 발생 총개수")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var great = rootView?.findViewById<ImageView>(R.id.great)
                            great?.visibility = View.VISIBLE
                            val drumHiHatFragment = DrumHiHatFragment()
                            transaction.replace(R.id.drumContainer, drumHiHatFragment)
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                great?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit - 1 && xPosition <= correctHit + 1 && boolean == 0) {
                        boolean = 1
                        System.out.println("3.1에서 발생 xPosition : $xPosition")
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3.1에서 발생 총개수")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var perfect = rootView?.findViewById<ImageView>(R.id.perfect)
                            perfect?.visibility = View.VISIBLE
                            val drumHiHatFragment = DrumHiHatFragment()
                            transaction.replace(R.id.drumContainer, drumHiHatFragment)
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                perfect?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
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

                    if (xPosition >= correctHit + 2 && xPosition <= correctHit + 3 && boolean == 0) {
                        boolean = 1
                        System.out.println("3.2에서 발생 xPosition : $xPosition")
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat" || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3.2에서 발생 총개수")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var good = rootView?.findViewById<ImageView>(R.id.good)
                            good?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                good?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit + 1 && xPosition <= correctHit + 2 && boolean == 0) {
                        boolean = 1
                        System.out.println("3.2에서 발생 xPosition : $xPosition")
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat" || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3.2에서 발생 총개수")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            //hit 표시
                            hit?.visibility = View.VISIBLE
                            var great = rootView?.findViewById<ImageView>(R.id.great)
                            great?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                great?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit - 1 && xPosition <= correctHit + 1 && boolean == 0) {
                        boolean = 1
                        System.out.println("3.2에서 발생 xPosition : $xPosition")
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat" || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3.2에서 발생 총개수")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var perfect = rootView?.findViewById<ImageView>(R.id.perfect)
                            perfect?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                perfect?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                    }
                }
                animator.start()


                //하이햇, 스네어 - 곰세마리, 나비야, 거미
            }else if (drumValuesArray.toString() == "[3,8]") {
                val drumHiHatFragment3 = DrumHiHatFragment3()
                val drumSnareFragment = DrumSnareFragment()

                transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    0
                )
                transaction.add(R.id.drumContainer, drumHiHatFragment3)
                transaction.add(R.id.drumContainer, drumSnareFragment)

                // ValueAnimator 초기화
                val animator = ValueAnimator.ofFloat(xPositionStart, xPositionEnd)
                animator.duration = 3000 // 애니메이션 지속 시간 (밀리초)

                animator.addUpdateListener { animation ->
                    val xPosition = animation.animatedValue as Float // 현재 애니메이션 중인 x 좌표

                    if (xPosition >= correctHit + 2 && xPosition <= correctHit + 3 && boolean == 0) {
                        boolean = 1
                        System.out.println("3,8에서 발생 xPosition : $xPosition")
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3,8에서 하이햇 발생 총개수")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var good = rootView?.findViewById<ImageView>(R.id.good)
                            good?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                good?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment1 == "snare"){
                            totalHit+=1
                            System.out.println("$totalHit 3,8에서 스네어 발생 총개수")
                            var snare = rootView?.findViewById<TextView>(R.id.hitSnare)
                            snare?.visibility = View.VISIBLE
                            var good = rootView?.findViewById<ImageView>(R.id.good)
                            good?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                snare?.visibility = View.INVISIBLE
                                good?.visibility = View.INVISIBLE
                                sharedViewModel.data1 = ""
                                dataFromCameraFragment1 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit + 1 && xPosition <= correctHit + 2 && boolean == 0) {
                        boolean = 1
                        System.out.println("3,8에서 발생 xPosition : $xPosition")
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3,8에서 하이햇 발생 총개수")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var great = rootView?.findViewById<ImageView>(R.id.great)
                            great?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                great?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment1 == "snare"){
                            totalHit+=1
                            System.out.println("$totalHit 3,8에서 스네어 발생 총개수")
                            var snare = rootView?.findViewById<TextView>(R.id.hitSnare)
                            snare?.visibility = View.VISIBLE
                            var great = rootView?.findViewById<ImageView>(R.id.great)
                            great?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                snare?.visibility = View.INVISIBLE
                                great?.visibility = View.INVISIBLE
                                sharedViewModel.data1 = ""
                                dataFromCameraFragment1 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit - 1 && xPosition <= correctHit + 1 && boolean == 0) {
                        boolean = 1
                        System.out.println("3,8에서 발생 xPosition : $xPosition")
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            System.out.println("$totalHit 3,8에서 하이햇 발생 총개수")
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var perfect = rootView?.findViewById<ImageView>(R.id.perfect)
                            perfect?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                perfect?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment1 == "snare"){
                            totalHit+=1
                            System.out.println("$totalHit 3,8에서 스네어 발생 총개수")
                            var snare = rootView?.findViewById<TextView>(R.id.hitSnare)
                            snare?.visibility = View.VISIBLE
                            var perfect = rootView?.findViewById<ImageView>(R.id.perfect)
                            perfect?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                snare?.visibility = View.INVISIBLE
                                perfect?.visibility = View.INVISIBLE
                                sharedViewModel.data1 = ""
                                dataFromCameraFragment1 = ""
                            }, 200)
                        }
                    }
                }
                animator.start()


                //하이햇, 베이스 - 나비야, 거미
            } else if (drumValuesArray.toString() == "[3,10]") {
                val drumHiHatFragment2 = DrumHiHatFragment2()
                val drumBassFragment = DrumBassFragment()

                transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    0
                )
                transaction.add(R.id.drumContainer, drumHiHatFragment2)
                transaction.add(R.id.drumContainer, drumBassFragment)

                // ValueAnimator 초기화
                val animator = ValueAnimator.ofFloat(xPositionStart, xPositionEnd)
                animator.duration = 3000 // 애니메이션 지속 시간 (밀리초)

                animator.addUpdateListener { animation ->
                    val xPosition = animation.animatedValue as Float // 현재 애니메이션 중인 x 좌표


                    // X 좌표가 40dp +- 1일 때의 처리
                    if (xPosition >= correctHit + 1.25 && xPosition <= correctHit + 1.5 && boolean == 0) {
                        boolean = 1
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var good = rootView?.findViewById<ImageView>(R.id.good)
                            good?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                good?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment9 == "bass"){
                            totalHit+=1
                            var bass = rootView?.findViewById<TextView>(R.id.hitBass)
                            bass?.visibility = View.VISIBLE
                            var good = rootView?.findViewById<ImageView>(R.id.good)
                            good?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                bass?.visibility = View.INVISIBLE
                                good?.visibility = View.INVISIBLE
                                sharedViewModel.data9 = ""
                                dataFromCameraFragment9 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit + 1 && xPosition <= correctHit + 1.25 && boolean == 0) {
                        boolean = 1
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var great = rootView?.findViewById<ImageView>(R.id.great)
                            great?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                great?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment9 == "bass"){
                            totalHit+=1
                            var bass = rootView?.findViewById<TextView>(R.id.hitBass)
                            bass?.visibility = View.VISIBLE
                            var great = rootView?.findViewById<ImageView>(R.id.great)
                            great?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                bass?.visibility = View.INVISIBLE
                                great?.visibility = View.INVISIBLE
                                sharedViewModel.data9 = ""
                                dataFromCameraFragment9 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit - 0.5 && xPosition <= correctHit + 1 && boolean == 0) {
                        boolean = 1
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var perfect = rootView?.findViewById<ImageView>(R.id.perfect)
                            perfect?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                perfect?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment9 == "bass"){
                            totalHit+=1
                            var bass = rootView?.findViewById<TextView>(R.id.hitBass)
                            bass?.visibility = View.VISIBLE
                            var perfect = rootView?.findViewById<ImageView>(R.id.perfect)
                            perfect?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                bass?.visibility = View.INVISIBLE
                                perfect?.visibility = View.INVISIBLE
                                sharedViewModel.data9 = ""
                                dataFromCameraFragment9 = ""
                            }, 200)
                        }
                    }
                }
                animator.start()


                //크래쉬, 베이스 - 작은별, 거미
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
                    if (xPosition >= correctHit + 1.25 && xPosition <= correctHit + 1.5 && boolean == 0) {
                        boolean = 1
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment2 == "crash"){
                            totalHit+=1
                            var crash = rootView?.findViewById<TextView>(R.id.hitCrash)
                            crash?.visibility = View.VISIBLE
                            var good = rootView?.findViewById<ImageView>(R.id.good)
                            good?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                crash?.visibility = View.INVISIBLE
                                good?.visibility = View.INVISIBLE
                                sharedViewModel.data2 = ""
                                dataFromCameraFragment2 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment9 == "bass"){
                            totalHit+=1
                            var bass = rootView?.findViewById<TextView>(R.id.hitBass)
                            bass?.visibility = View.VISIBLE
                            var good = rootView?.findViewById<ImageView>(R.id.good)
                            good?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                bass?.visibility = View.INVISIBLE
                                good?.visibility = View.INVISIBLE
                                sharedViewModel.data9 = ""
                                dataFromCameraFragment9 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit + 1 && xPosition <= correctHit + 1.25 && boolean == 0) {
                        boolean = 1
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment2 == "crash"){
                            totalHit+=1
                            var crash = rootView?.findViewById<TextView>(R.id.hitCrash)
                            crash?.visibility = View.VISIBLE
                            var great = rootView?.findViewById<ImageView>(R.id.great)
                            great?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                crash?.visibility = View.INVISIBLE
                                great?.visibility = View.INVISIBLE
                                sharedViewModel.data2 = ""
                                dataFromCameraFragment2 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment9 == "bass"){
                            totalHit+=1
                            var bass = rootView?.findViewById<TextView>(R.id.hitBass)
                            bass?.visibility = View.VISIBLE
                            var great = rootView?.findViewById<ImageView>(R.id.great)
                            great?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                bass?.visibility = View.INVISIBLE
                                great?.visibility = View.INVISIBLE
                                sharedViewModel.data9 = ""
                                dataFromCameraFragment9 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit - 0.5 && xPosition <= correctHit + 1 && boolean == 0) {
                        boolean = 1
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment2 == "crash"){
                            totalHit+=1
                            var crash = rootView?.findViewById<TextView>(R.id.hitCrash)
                            crash?.visibility = View.VISIBLE
                            var perfect = rootView?.findViewById<ImageView>(R.id.good)
                            perfect?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                crash?.visibility = View.INVISIBLE
                                perfect?.visibility = View.INVISIBLE
                                sharedViewModel.data2 = ""
                                dataFromCameraFragment2 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment9 == "bass"){
                            totalHit+=1
                            var bass = rootView?.findViewById<TextView>(R.id.hitBass)
                            bass?.visibility = View.VISIBLE
                            var perfect = rootView?.findViewById<ImageView>(R.id.good)
                            perfect?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                bass?.visibility = View.INVISIBLE
                                perfect?.visibility = View.INVISIBLE
                                sharedViewModel.data9 = ""
                                dataFromCameraFragment9 = ""
                            }, 200)
                        }
                    }
                }
                animator.start()


                //크래쉬, 스네어 - 거미
            }else if (drumValuesArray.toString() == "[1,8]") {
                val drumCrashFragment = DrumCrashFragment()
                val drumhiHatFragment = DrumHiHatFragment()

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
                    if (xPosition >= correctHit + 1.25 && xPosition <= correctHit + 1.5 && boolean == 0) {
                        boolean = 1
                        if(dataFromCameraFragment2 == "crash"){
                            totalHit+=1
                            var crash = rootView?.findViewById<TextView>(R.id.hitCrash)
                            crash?.visibility = View.VISIBLE
                            var good = rootView?.findViewById<ImageView>(R.id.good)
                            good?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                crash?.visibility = View.INVISIBLE
                                good?.visibility = View.INVISIBLE
                                sharedViewModel.data2 = ""
                                dataFromCameraFragment2 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var good = rootView?.findViewById<TextView>(R.id.good)
                            good?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                good?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit + 1 && xPosition <= correctHit + 1.25 && boolean == 0) {
                        boolean = 1
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment2 == "crash"){
                            totalHit+=1
                            var crash = rootView?.findViewById<TextView>(R.id.hitCrash)
                            crash?.visibility = View.VISIBLE
                            var great = rootView?.findViewById<TextView>(R.id.good)
                            great?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                crash?.visibility = View.INVISIBLE
                                great?.visibility = View.INVISIBLE
                                sharedViewModel.data2 = ""
                                dataFromCameraFragment2 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var great = rootView?.findViewById<TextView>(R.id.good)
                            great?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                great?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                    }else if (xPosition >= correctHit - 0.5 && xPosition <= correctHit + 1 && boolean == 0) {
                        boolean = 1
                        //만약 지금 친 드럼이 hihat이라면
                        if(dataFromCameraFragment2 == "crash"){
                            totalHit+=1
                            var crash = rootView?.findViewById<TextView>(R.id.hitCrash)
                            crash?.visibility = View.VISIBLE
                            var perfect = rootView?.findViewById<TextView>(R.id.perfect)
                            perfect?.visibility = View.VISIBLE
                            //0.2초 뒤 사라지게 하고 값도 제거
                            Handler().postDelayed({
                                crash?.visibility = View.INVISIBLE
                                perfect?.visibility = View.INVISIBLE
                                sharedViewModel.data2 = ""
                                dataFromCameraFragment2 = ""
                            }, 200)
                        }
                        if(dataFromCameraFragment == "openHiHat"  || dataFromCameraFragment4 == "closedHat"){
                            totalHit+=1
                            var hit = rootView?.findViewById<TextView>(R.id.hitHiHat)
                            hit?.visibility = View.VISIBLE
                            var perfect = rootView?.findViewById<TextView>(R.id.perfect)
                            perfect?.visibility = View.VISIBLE
                            Handler().postDelayed({
                                hit?.visibility = View.INVISIBLE
                                perfect?.visibility = View.INVISIBLE
                                sharedViewModel.data = ""
                                sharedViewModel.data4 = ""
                                dataFromCameraFragment = ""
                                dataFromCameraFragment4 = ""
                            }, 200)
                        }
                    }
                }
                animator.start()
            }
            transaction.commit()
        }
    }
}