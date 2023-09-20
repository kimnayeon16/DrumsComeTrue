package com.ssafy.drumscometrue.commonresp

import com.google.gson.annotations.SerializedName

data class ErrorResp(
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)
