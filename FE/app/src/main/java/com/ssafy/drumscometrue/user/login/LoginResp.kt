package com.ssafy.drumscometrue.user.login

import com.google.gson.annotations.SerializedName

data class LoginResp(
    @SerializedName("userId") val userId: String?,
    @SerializedName("userPw") val userPw: String?
)
