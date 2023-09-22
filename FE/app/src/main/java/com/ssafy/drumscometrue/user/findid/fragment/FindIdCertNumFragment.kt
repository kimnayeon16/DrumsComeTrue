package com.ssafy.drumscometrue.user.findid.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.user.findid.FindIdActivity

class FindIdCertNumFragment : Fragment(R.layout.fragment_find_id_cert) {

    private lateinit var searchFindId: Button
    private lateinit var certNum: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchFindId = view.findViewById(R.id.search_find_id)
        certNum = view.findViewById(R.id.cert_num_text)

        // 이부분에 api 호출
        var testNum = "0"

        searchFindId.setOnClickListener {
            // 인증번호가 같으면 -> Success로 보내고
            if(certNum.text.toString() == testNum) {
                (activity as FindIdActivity).replaceFragment(FindIdSuccessFragment())
            }
            else {
                // 인증번호가 다르면 -> 실패 페이지로 보내기
                (activity as FindIdActivity).replaceFragment(FindIdFailFragment())
            }
        }
    }

}