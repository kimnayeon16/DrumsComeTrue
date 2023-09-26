package com.ssafy.drumscometrue.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonSyntaxException
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.retrofit2.ApiService
import com.ssafy.drumscometrue.user.join.JoinReq
import com.ssafy.drumscometrue.user.login.LoginActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JoinActivity : AppCompatActivity() {

    // Text
    private lateinit var userIdText: EditText
    private lateinit var userPwText: EditText
    private lateinit var userPwCheckText: EditText
    private lateinit var userNameText: EditText

    // Btn
    private lateinit var userIdCheckBtn: Button
    private lateinit var joinSubmitBtn: Button

    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        initUi()
        setupListener()
    }

    private fun initUi() {
        // Text
        userIdText = findViewById<EditText>(R.id.user_id)
        userPwText = findViewById<EditText>(R.id.user_pwd)
        userPwCheckText = findViewById<EditText>(R.id.user_pwd_check)
        userNameText = findViewById<EditText>(R.id.user_name)

        // Btn
        userIdCheckBtn = findViewById<Button>(R.id.user_id_dp_check)
        joinSubmitBtn = findViewById<Button>(R.id.join_submit)


        retrofit = Retrofit.Builder()
            .baseUrl("https://j9b107.p.ssafy.io:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private fun setupListener() {

        // 아이디 중복 확인
        userIdCheckBtn.setOnClickListener { handleUserIdCheckClick() }

        // 회원 가입
        joinSubmitBtn.setOnClickListener { handleJoinSubmitClick() }

    }

    private fun handleUserIdCheckClick() {
        // 버튼 리스너 설정
        val userId = userIdText.text.toString()

        // 호출해서 error 나오면 중복되었습니다 modal
        apiService.checkUserId(userId)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("HTTP Status Code", response.code().toString())
                    try {
                        if (response.isSuccessful) {
                            // 사용 가능한 아이디
                            val dialog = AlertDialog.Builder(this@JoinActivity)
                                .setMessage("사용 가능한 아이디 입니다.")
                                .setPositiveButton("확인") { _, _ -> }
                                .create()

                            dialog.show()
                        } else {
                            // 사용 가능한 아이디
                            val dialog = AlertDialog.Builder(this@JoinActivity)
                                .setMessage("중복된 아이디 입니다.")
                                .setPositiveButton("확인") { _, _ -> }
                                .create()

                            dialog.show()
                        }
                    } catch (e: JsonSyntaxException) {
                        Log.d("error", "JSON 파싱 오류: ")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("Resp onFailure?", "실행됨")
                }
            })

    }

    private fun handleJoinSubmitClick() {
        val joinReq = JoinReq(
            userId = userIdText.text.toString(),
            userPw = userPwText.text.toString(),
            userName = userNameText.text.toString()
        )

        apiService.join(joinReq)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("HTTP Status Code", response.code().toString())
                    try {
                        if (response.isSuccessful) {
                            // 회원가입 성공 다이얼로그 띄우기
                            val dialog = AlertDialog.Builder(this@JoinActivity)
                                .setTitle("회원가입 성공")
                                .setMessage("회원가입이 완료되었습니다.")
                                .setPositiveButton("확인") { _, _ ->
                                    // 메인 페이지로 이동
                                    val intent = Intent(this@JoinActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                                .create()

                            dialog.show()
                        } else {
                            // 회원가입 실패
                            val dialog = AlertDialog.Builder(this@JoinActivity)
                                .setTitle("회원가입 실패")
                                .setMessage("정보를 다시 확인해주세요.")
                                .setPositiveButton("확인") { _, _ -> }
                                .create()

                            dialog.show()
                        }
                    } catch (e: JsonSyntaxException) {
                        Log.d("error", "JSON 파싱 오류: ")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("Resp onFailure?", "실행됨")
                }
            })
    }
}