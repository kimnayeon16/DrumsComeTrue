/*
 * Copyright 2023 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ssafy.drumscometrue.freePlay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.databinding.ActivityFreePlayBinding
import com.ssafy.drumscometrue.persistentbottom.PersistentFragment

class FreePlayActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityFreePlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Activity의 layout인플레이트 -> 결과를 activityMainBinding객체에 바인딩
        activityMainBinding = ActivityFreePlayBinding.inflate(layoutInflater)
        // activityMainBinding에 바인딩된 레이아웃을 액티비티의 컨텐츠로 설정합니다.
        setContentView(activityMainBinding.root)

        // PersistentFragment 추가
        if (savedInstanceState == null) { // 첫 생성 시만 Fragment 추가
            supportFragmentManager.beginTransaction()
                .replace(R.id.persistent_fragment_container, PersistentFragment())
                .commit()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}