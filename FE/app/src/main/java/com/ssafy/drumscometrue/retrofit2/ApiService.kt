package com.ssafy.drumscometrue.retrofit2

import com.ssafy.drumscometrue.user.login.LoginReq
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/v1/user/login")
    fun login(@Body req: LoginReq): Call<ResponseBody>
}