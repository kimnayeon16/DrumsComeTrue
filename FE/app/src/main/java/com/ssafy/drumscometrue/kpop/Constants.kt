package com.ssafy.drumscometrue.kpop

object Constants {
    fun getKpopSongs(): ArrayList<KpopList>{
        val songsList = ArrayList<KpopList>()

        val song1 =KpopList(
            1, "곡이름1", "가수이름1", 1
        )
        songsList.add(song1)

        val song2 =KpopList(
            3, "곡이름2", "가수이름2", 1
        )
        songsList.add(song1)

        val song3 =KpopList(
            3, "곡이름3", "가수이름3", 1
        )
        songsList.add(song1)

        val song4 =KpopList(
            4, "곡이름4", "가수이름4", 1
        )
        songsList.add(song1)

        val song5 =KpopList(
            6, "곡이름6", "가수이름5", 1
        )
        songsList.add(song1)

        return songsList
    }
}