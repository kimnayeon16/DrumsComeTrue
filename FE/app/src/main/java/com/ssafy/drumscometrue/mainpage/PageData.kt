package com.ssafy.drumscometrue.mainpage

data class PageData(
    val img: Int, // 이미지 리소스 ID
    val title: String,
    val description: String,
    val targetPage:Int // 이동 할 페이지 번호
)