package com.ssafy.drumscometrue.user.findid.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.user.findid.FindIdActivity

class FindIdMainFragment : Fragment(R.layout.fragment_find_id_main) {
    private lateinit var submitCertNumBtn: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submitCertNumBtn = view.findViewById(R.id.submit_cert_num)

        submitCertNumBtn.setOnClickListener {
            (activity as FindIdActivity).replaceFragment(FindIdCertNumFragment())
        }
    }
}