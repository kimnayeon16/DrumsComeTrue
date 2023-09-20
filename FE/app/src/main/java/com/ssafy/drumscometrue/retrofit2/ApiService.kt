package com.ssafy.drumscometrue.retrofit2

import com.ssafy.drumscometrue.user.login.LoginReq
import com.ssafy.drumscometrue.user.login.LoginRes
import okhttp3.ResponseBody
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
    ): Call<ResponseBody>
}