package com.ssafy.drumscometrue.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.retrofit2.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    // Retrofit set
    val retrofit = Retrofit.Builder()
        .baseUrl("https://j9b107.p.ssafy.io:8000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
    // end


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Text
        var loginIdText = findViewById<EditText>(R.id.login_id)
        var loginPwdText = findViewById<EditText>(R.id.login_pwd)
        var findIdText = findViewById<TextView>(R.id.find_user_id)
        var findPwdText = findViewById<TextView>(R.id.find_user_pwd)
        var joinText = findViewById<TextView>(R.id.join_user)

        // Btn
        val loginBtn = findViewById<Button>(R.id.login)

        // Image
        val kakaoLoginImg = findViewById<ImageView>(R.id.kakao_login)
        val naverLoginImg = findViewById<ImageView>(R.id.naver_login)

        // 로그인
        loginBtn.setOnClickListener {
            Log.d("실행됨?", "실행됨")
            val loginReq = LoginReq(
                loginId = loginIdText.text.toString(),
                loginPwd = loginPwdText.text.toString()
            )

            val call = apiService.login(loginReq)

            call.enqueue(object : Callback<LoginRes> {
                override fun onResponse(call: Call<LoginRes>, response: Response<LoginRes>) {
                    if(response.isSuccessful) {
                        // 성공적으로 로그인
                        Log.d("success", response.body().toString())
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()

//                        // 로그인 성공 시 메인페이지로 이동
//                        val intent = Intent(this@LoginActivity, MainPageActivity::class.java)
//                        startActivity(intent)
//                        finish()

                    } else {
                        // 실패 (400 Bad Request 등)
                        Log.d("error", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<LoginRes>, t: Throwable) {
                    // 네트워크 오류 등
                    Toast.makeText(this@LoginActivity, "오류: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // 회원가입
//        joinBtn.setOnClickListener {
//            var intent = Intent(this, JoinActivity::class.java)
//            startActivity(intent)
//        }
    }
}