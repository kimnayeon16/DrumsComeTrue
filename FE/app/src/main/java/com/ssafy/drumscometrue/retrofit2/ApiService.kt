package com.ssafy.drumscometrue.retrofit2

import com.ssafy.drumscometrue.user.join.JoinReq
import com.ssafy.drumscometrue.user.login.LoginReq
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/v1/user/login")
    fun login(@Body req: LoginReq): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/api/v1/user/signup")
    fun join(@Body req: JoinReq): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET("/api/v1/user/login/check/{userId}")
    fun checkUserId(@Path("userId") userId: String): Call<ResponseBody>
}