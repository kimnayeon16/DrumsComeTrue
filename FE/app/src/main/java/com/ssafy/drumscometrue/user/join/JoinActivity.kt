package com.ssafy.drumscometrue.user

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.retrofit2.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JoinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        // Text
        var userIdText = findViewById<EditText>(R.id.user_id)
        var userPwText = findViewById<EditText>(R.id.user_pwd)
        var userPwCheckText = findViewById<EditText>(R.id.user_pwd_check)
        var userNameText = findViewById<EditText>(R.id.user_name)
        var userPhoneText = findViewById<EditText>(R.id.user_phone)

        // Btn
        val userIdCheckBtn = findViewById<Button>(R.id.user_id_dp_check)
        val userPhoneCheckBtn = findViewById<Button>(R.id.user_phone_check)
        val joinSubmitBtn = findViewById<Button>(R.id.join_submit)

        // Retrofit set
        val retrofit = Retrofit.Builder()
            .baseUrl("https://j9b107.p.ssafy.io:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        // end

        // 아이디 중복 확인
        userIdCheckBtn.setOnClickListener {
            val userId = userIdText.text.toString()
            Toast.makeText(this, userId, Toast.LENGTH_SHORT).show()
        }

        // 핸드폰 인증
        userPhoneCheckBtn.setOnClickListener {
            val phoneNumber = userPhoneText.text.toString()
        }

        // 회원가입
        joinSubmitBtn.setOnClickListener {
            val userId = userIdText.text.toString()
            val userPw = userPwText.text.toString()
            val userName = userNameText.text.toString()

        }

    }

}