package com.ssafy.drumscometrue.user

import com.google.gson.annotations.SerializedName

data class LoginReq(
    @SerializedName("userId") val loginId: String?,
    @SerializedName("userPw") val loginPwd: String?
)
