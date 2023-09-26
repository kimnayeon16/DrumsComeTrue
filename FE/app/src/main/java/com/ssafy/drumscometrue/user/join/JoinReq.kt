package com.ssafy.drumscometrue.user.join

import com.google.gson.annotations.SerializedName

data class JoinReq(
    @SerializedName("userId") val userId: String?,
    @SerializedName("userName") val userName: String?,
    @SerializedName("userPw") val userPw: String?
)
