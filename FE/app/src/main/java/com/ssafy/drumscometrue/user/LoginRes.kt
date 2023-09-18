package com.ssafy.drumscometrue.user

import com.google.gson.annotations.SerializedName

data class LoginRes(
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)
