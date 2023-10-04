package com.ssafy.drumscometrue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    var data: String = "" //하이햇(오픈 하이햇?)
    var data1: String = "" //스네어
    var data2: String = "" //크래쉬
    var data3: String = "" //라이드
    var data4: String = "" //클로즈하이햇
    var data5: String = "" //
    var data6: String = "" //탐1
    var data7: String = "" //탐2
    var data8: String = "" //플로우
    var data9: String = "" //베이스
    var totalHit: Int = 0
//    베이스랑 풋하이햇 아직 안 넣음

    private val _dataLiveData = MutableLiveData<String>()

    // 외부에서 접근할 수 있는 LiveData
    val dataLiveData: LiveData<String>
        get() = _dataLiveData

    // 데이터 업데이트 메서드
    fun updateData(newData: String) {
        _dataLiveData.value = data
        _dataLiveData.value = data1
    }

}