package com.ssafy.drumscometrue.user.login

import com.google.gson.annotations.SerializedName

data class LoginReq(
    @SerializedName("userId") val loginId: String?,
    @SerializedName("userPw") val loginPwd: String?
)
