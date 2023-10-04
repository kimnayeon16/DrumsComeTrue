package com.ssafy.drumscometrue.user.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.freePlay.FreePlayActivity
import com.ssafy.drumscometrue.mainpage.MainPageActivity
import com.ssafy.drumscometrue.retrofit2.ApiService
import com.ssafy.drumscometrue.user.JoinActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    // Container
    private lateinit var mainUiContainer : FrameLayout
    private lateinit var loginOptionLayout : LinearLayout


    // Text
    private lateinit var loginIdText: EditText
    private lateinit var loginPwdText: EditText
    private lateinit var joinText: TextView
    private lateinit var joinBtn: TextView

    // Btn
    private lateinit var loginBtn: Button

    // Image
    private lateinit var kakaoLoginImg: ImageView
    private lateinit var naverLoginImg: ImageView

    // Retrofit
    private lateinit var gson: Gson
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    // Share
    private lateinit var userInfo: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUI() // UI 설정

        // Email과 Nickname 값을 SharedPreference에서 확인합니다.
        val userId = userInfo.getString("userId", null)

        // Email과 Nickname 값이 존재하면 (즉, 이미 로그인한 사용자라면) MainPageActivity로 이동합니다.
        if (userId != null) {
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
            finish() // LoginActivity를 종료하여 뒤로 가기를 눌렀을 때 다시 로그인 화면으로 돌아오지 않게 합니다.
            return
        }

        val keyHash = Utility.getKeyHash(this)
        KakaoSdk.init(this, "9d9f74e5dee02dda5e82ada45e4dda8f")
        setupListeners() // Listners 설정
    }

    private fun initUI() {
        // 여기에서 UI 요소들을 초기화할 수 있습니다.
        mainUiContainer = findViewById(R.id.main_ui_container)
        loginOptionLayout = findViewById(R.id.login_option_layout)

        loginIdText = findViewById(R.id.login_id)
        loginPwdText = findViewById(R.id.login_pwd)
        joinText = findViewById(R.id.join_user)
        joinBtn = findViewById(R.id.join_user)

        loginBtn = findViewById(R.id.login)
        kakaoLoginImg = findViewById(R.id.kakao_login)
        naverLoginImg = findViewById(R.id.naver_login)

        // Retrofit 초기화
        gson = GsonBuilder().setLenient().create()

        retrofit = Retrofit.Builder()
            .baseUrl("https://j9b107.p.ssafy.io:8000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Share
        userInfo = getSharedPreferences("user_info", MODE_PRIVATE)
    }

    private fun setupListeners() {
        // 버튼 리스너 설정
        loginBtn.setOnClickListener { handleLoginButtonClick() }
        joinBtn.setOnClickListener { handleJoinButtonClick() }
        kakaoLoginImg.setOnClickListener { handleKakaoLoginButtonClick() }

        val drumTestBtn = findViewById<Button>(R.id.drum_test)
        drumTestBtn.setOnClickListener { handleDrumTestButtonClick() }
    }

    // 로그인
    private fun handleLoginButtonClick() {
        val loginReq = LoginReq(
            loginId = loginIdText.text.toString(),
            loginPwd = loginPwdText.text.toString()
        )

        apiService.login(loginReq)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("HTTP Status Code", response.code().toString())
                    try {
                        if (response.isSuccessful) {
                            // 응답 헤더에서 원하는 값을 가져옵니다.
                            val accessT = response.headers()["Authorization"]
                            val refreshT = response.headers()["Authorization-refresh"]
                            val userPk = response.headers()["userPk"]

                            // 값을 기기에 저장
                            userInfo.edit {
                                putString("userId", loginReq.loginId)
                                // 저장하고자 하는 헤더 값을 확인한 후 저장합니다.
                                if (accessT != null) {
                                    putString("accessT", accessT)
                                }
                                if (refreshT != null) {
                                    putString("refreshT", refreshT)
                                }
                                if (userPk != null) {
                                    putString("userPk", userPk)
                                }
                            }

                            // 메인 페이지로 이동
                            val intent = Intent(this@LoginActivity, MainPageActivity::class.java)
                            startActivity(intent)
                        } else {
                            // 로그인 실패 다이얼로그 띄우기
                            val dialog = AlertDialog.Builder(this@LoginActivity)
                                .setTitle("로그인 실패")
                                .setMessage("로그인 정보를 확인해주세요.")
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

    // 회원가입
    private fun handleJoinButtonClick() {
        val intent = Intent(this, JoinActivity::class.java)
        startActivity(intent)
    }

    private fun handleDrumTestButtonClick() {
        val intent = Intent(this, FreePlayActivity::class.java)
        startActivity(intent)
    }

    private fun handleKakaoLoginButtonClick() {
        if(LoginClient.instance.isKakaoTalkLoginAvailable(this)){
            Log.d("available", "call")
            LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
        }else{
            Log.d("available", "fail")
            LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)

        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token}")

            // 여기서 사용자 정보를 요청합니다.
            UserApiClient.instance.me { user, userError ->
                if (userError != null) {
                    Log.e(TAG, "사용자 정보 요청 실패", userError)
                } else if (user != null) {
                    val email = user.kakaoAccount?.email
                    val nickname = user.kakaoAccount?.profile?.nickname
                    Log.i(TAG, "Email: $email, Nickname: $nickname")

                    userInfo.edit {
                        putString("userId", email)
                        putString("userName", nickname)
                    }
                }
            }

            // 메인 페이지로 이동
            val intent = Intent(this@LoginActivity, MainPageActivity::class.java)
            startActivity(intent)
        }
    }

}