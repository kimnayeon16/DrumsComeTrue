package com.ssafy.drumscometrue.kpop

data class Kpop(
    val song: String, //노래 제목
    val singer: String, //가수
    val image: String, //이미지
    val score: String, //악보
    val prelude: Long, //전주 시간
    val interval: Long, //간격
    val songTotalTime: Long, //노래 전체 시간
    val level: String
)
