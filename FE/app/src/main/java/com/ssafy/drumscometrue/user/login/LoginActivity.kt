package com.ssafy.drumscometrue.user.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.retrofit2.ApiService
import com.ssafy.drumscometrue.user.JoinActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Text
        var loginIdText = findViewById<EditText>(R.id.login_id)
        var loginPwdText = findViewById<EditText>(R.id.login_pwd)
        var findIdText = findViewById<TextView>(R.id.find_user_id)
        var findPwdText = findViewById<TextView>(R.id.find_user_pwd)
        var joinText = findViewById<TextView>(R.id.join_user)
        val joinBtn = findViewById<TextView>(R.id.join_user)

        // Btn
        val loginBtn = findViewById<Button>(R.id.login)

        // Image
        val kakaoLoginImg = findViewById<ImageView>(R.id.kakao_login)
        val naverLoginImg = findViewById<ImageView>(R.id.naver_login)

        // Retrofit set
        val gson= GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://j9b107.p.ssafy.io:8000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        // end

        // 로그인
        loginBtn.setOnClickListener {
            val loginReq = LoginReq(
                loginId = loginIdText.text.toString(),
                loginPwd = loginPwdText.text.toString()
            )

            apiService.login(loginReq)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>)
                    {
                        if(response.isSuccessful) {
                            val responseBodyString = response.body()?.string() ?: "Empty Response"
                            Log.d("success", responseBodyString)
                            Toast.makeText(this@LoginActivity, "로그인 성공: $responseBodyString", Toast.LENGTH_SHORT).show()
                            // 메인 페이지로 이동
                        } else {
                            Log.d("error", response.errorBody()?.string() ?: "Error Response")
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseBody>,
                        t: Throwable)
                    {
                        // 네트워크 오류 등
                        Toast.makeText(this@LoginActivity, "오류: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

        }

        // 회원가입
        joinBtn.setOnClickListener {
            var intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
    }
}