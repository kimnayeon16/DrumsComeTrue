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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.commonresp.ErrorResp
import com.ssafy.drumscometrue.freePlay.FreePlayActivity
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
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Log.d("Resp onResp?", "실행됨")
                        val responseBodyString = response.body()?.string() ?: response.errorBody()?.string() ?: "Empty Response"
                        try {
                            if(response.isSuccessful) {
                                val loginResponse = Gson().fromJson(responseBodyString, LoginResp::class.java)
                                val userId = loginResponse.userId ?: "Unknown User ID"
                                val userPw = loginResponse.userPw ?: "Unknown Password"
                                Log.d("success", loginResponse.toString())
                                Toast.makeText(this@LoginActivity, "로그인 성공: ID: $userId, PW: $userPw", Toast.LENGTH_SHORT).show()
                            } else {
                                val errorResponse = Gson().fromJson(responseBodyString, ErrorResp::class.java)
                                val code = errorResponse.code
                                val message = errorResponse.message

                                Log.d("error", errorResponse.message)
                                Toast.makeText(this@LoginActivity, "오류: ${errorResponse.message}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JsonSyntaxException) {
                            Log.d("error", "JSON 파싱 오류: $responseBodyString")
                            Toast.makeText(this@LoginActivity, "파싱 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("Resp onFailure?", "실행됨")
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

        // Drum Test
        val drumTestBtn = findViewById<Button>(R.id.drum_test)

        drumTestBtn.setOnClickListener {
            var intent = Intent(this, FreePlayActivity::class.java)
            startActivity(intent)
        }
    }
}