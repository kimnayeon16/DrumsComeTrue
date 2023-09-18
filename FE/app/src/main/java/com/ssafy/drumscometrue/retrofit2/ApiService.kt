package com.ssafy.drumscometrue.retrofit2

import com.ssafy.drumscometrue.user.LoginReq
import com.ssafy.drumscometrue.user.LoginRes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    companion object {
        const val endPoint = "/api/v1/user"
    }

    @POST("${endPoint}/login")
    fun login(
        @Body req: LoginReq
    ): Call<LoginRes>
}