package com.ssafy.drumscometrue.mainpage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import com.ssafy.drumscometrue.R

class TutorialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tutorial, container, false)

        // 클릭 이벤트를 처리할 View를 찾습니다. 예를 들어, 클릭 이벤트를 처리할 View의 ID가 btnNavigateToPage 인 경우:
        val cardLayout = view.findViewById<LinearLayoutCompat>(R.id.page_linear_tutor)

        cardLayout.setOnClickListener {
            // 클릭되었을 때 프래그먼트 전환을 수행합니다.
            val intent = Intent(requireContext(), TutorialPageActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}