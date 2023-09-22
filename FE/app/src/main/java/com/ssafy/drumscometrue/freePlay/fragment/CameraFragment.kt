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
package com.ssafy.drumscometrue.freePlay.fragment

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Camera
import androidx.camera.core.AspectRatio
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.ssafy.drumscometrue.freePlay.MainViewModel
import com.ssafy.drumscometrue.R
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.ssafy.drumscometrue.databinding.FragmentCameraBinding
import com.ssafy.drumscometrue.freePlay.PoseLandmarkerHelper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


/**
 * 카메라를 사용하여 실시간으로 사용자의 포즈를 감지하고 표시하는 데 사용되는 프래그먼트
 * TensorFlow와 CameraX라이브러리를 사용하여 구현
 */
class CameraFragment : Fragment(), PoseLandmarkerHelper.LandmarkerListener {

    /**
     * 클래스 내부에 정적인 변수와 메서드를 선언하는 데 사용
     * */
    companion object {
        private const val TAG = "Pose Landmarker"
    }

    //
    private var _fragmentCameraBinding: FragmentCameraBinding? = null

    private val fragmentCameraBinding
        get() = _fragmentCameraBinding!!

    private lateinit var poseLandmarkerHelper: PoseLandmarkerHelper

    private val viewModel: MainViewModel by activityViewModels()
    //카메라 미리보기 및 이미지 분석과 관련된 변수들 -> glSurfaceView로 변경해도 될듯
    private var preview: Preview? = null    // 카메라에서 가져온 실시간 미리보기 화면을 표시하는 데 사용
    private var imageAnalyzer: ImageAnalysis? = null    //카메라로부터 가져온 이미지 프레임을 분석하고 처리하는 데 사용
    private var camera: Camera? = null
    //카메라 공급자 인스턴스를 저장하는 변수
    private var cameraProvider: ProcessCameraProvider? = null
    //현재 사용중인 카메라의 렌즈 방향 저장하는 함수
    private var cameraFacing = CameraSelector.LENS_FACING_FRONT

    /** Blocking ML operations are performed using this executor */
    private lateinit var backgroundExecutor: ExecutorService

    private lateinit var soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()

    private var start = false
    private var leftHandEstimation = mutableMapOf<String, Boolean>(
        "crash" to false,
        "ride" to false,
        "hiHat" to false,
        "hTom" to false,
        "mTom" to false,
        "floorTom" to false,
        "snare" to false
    )
    private var rightHandEstimation = mutableMapOf<String, Boolean>(
        "crash" to false,
        "ride" to false,
        "hiHat" to false,
        "hTom" to false,
        "mTom" to false,
        "floorTom" to false,
        "snare" to false
    )
    private var leftHihat : Boolean = false
    private var rightBass : Boolean = false


    /**
     * Fragment가 화면에 나타날 때 호출
     * 필요 권한 확인
     * PoseLandmarkerHelper를 다시 시작
     * */
    override fun onResume() {
        super.onResume()
        // 앱의 권한확인
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(   //Jetpack Navigation라이브러리 : 네비게이션 그래프에서 검색, 가져오는데 사용
                requireActivity(), R.id.find_id_ui_fragment  //requireActivity : 현재 속한 컨텍스트, R.id.fragment_container: 네비게이션 그래프에서 화면 간 전환을 관리하는 호스트 컨테이너
            ).navigate(R.id.action_camera_to_permissions)
        }

        // PoseLandmarkerHelper 재시작
        backgroundExecutor.execute {    // 백그라운드 스레드에서 작업 수행
            if(this::poseLandmarkerHelper.isInitialized) {//초기화 됨?
                if (poseLandmarkerHelper.isClose()) {//종료됨?
                    poseLandmarkerHelper.setupPoseLandmarker()
                }
            }
        }
    }

    /**
     * Fragment가 일시 중지 시 호출
     * PoseLandmarkerHelper를 종료하고 필요한 상태를 저장
     * */
    override fun onPause() {
        super.onPause()
        if(this::poseLandmarkerHelper.isInitialized) {  //초기화확인
            viewModel.setMinPoseDetectionConfidence(poseLandmarkerHelper.minPoseDetectionConfidence)
            viewModel.setMinPoseTrackingConfidence(poseLandmarkerHelper.minPoseTrackingConfidence)
            viewModel.setMinPosePresenceConfidence(poseLandmarkerHelper.minPosePresenceConfidence)
            viewModel.setDelegate(poseLandmarkerHelper.currentDelegate)

            // 백그라운드 스레드에서 실행할 코드 블록을 정의
            // 안드로이드 앱은 메인 스레드에서 UI를 처리
            // 시간이 걸리는 작업을 메인에서 하면 UI가 끊어질 수 있어 백그라운드 스레드에서 처리
            backgroundExecutor.execute { poseLandmarkerHelper.clearPoseLandmarker() }
        }
    }


    /**
     * Fragment의 뷰가 파괴될 때 호출
     * 백그라운드 스레드를 종료
     * */
    override fun onDestroyView() {
        _fragmentCameraBinding = null
        super.onDestroyView()

        // Shut down our background executor
        backgroundExecutor.shutdown()
        backgroundExecutor.awaitTermination(
            Long.MAX_VALUE, TimeUnit.NANOSECONDS
        )
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setSound()
    }


    /**
     * Fragment의 뷰를 생성하고 해당 뷰를 반환
     * */
    override fun onCreateView(
        inflater: LayoutInflater,   // xml레이아웃 파일을 fragment의 뷰ㅜ로 생성하는 데사용
        container: ViewGroup?,  // fragment가 연결될 부모 뷰 그룹을 나타냄, 일반적으로 Fragment는 Activity의 레이아웃 내에서 특정 위치에 추가됨 이때 container는 해당위치를 가리킴
        savedInstanceState: Bundle? //fragment의 상태 저장 및 복원하는데 사용
    ): View {
        //FragmentCameraBinding클래스를 사용하여 뷰와 데이터 바인딩을 생성
        _fragmentCameraBinding =
            FragmentCameraBinding.inflate(inflater, container, false)

        // fragment의 실제 뷰
        return fragmentCameraBinding.root
    }


    /**
     * 뷰가 생성된 후 호출
     * 백그라운드 스레드를 초기화
     * PoseLandmarkerHelper를 생성
     * */
    @SuppressLint("MissingPermission") //Android앱의 어노테이션 Lint도구에 의해 감지되는 경고 무시하도록
    //권한 검사 없이 권한이 필요한 작업을 수행하는 코드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize our background executor
        backgroundExecutor = Executors.newSingleThreadExecutor()

        // Wait for the views to be properly laid out
        fragmentCameraBinding.viewFinder.post {
            // Set up the camera and its use cases
            setUpCamera()
        }

        // Create the PoseLandmarkerHelper that will posele the inference
        backgroundExecutor.execute {
            poseLandmarkerHelper = PoseLandmarkerHelper(
                context = requireContext(),
                runningMode = RunningMode.LIVE_STREAM,
                minPoseDetectionConfidence = viewModel.currentMinPoseDetectionConfidence,
                minPoseTrackingConfidence = viewModel.currentMinPoseTrackingConfidence,
                minPosePresenceConfidence = viewModel.currentMinPosePresenceConfidence,
                currentDelegate = viewModel.currentDelegate,
                poseLandmarkerHelperListener = this
            )
        }
    }


    /**
     * 카메라 초기화
     * 미리보기와 이미지 분석 설정
     * */
    private fun setUpCamera() {
        // ProcessCameraProvider인스턴스를 가져오는 비동기 작업 시작 -> 카메라 프로바이더 얻을 수 있음
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(requireContext())//requireContext()현재 Fragment의 컨텍스트를 가져옴
        // 위의 작업 종료 시 콜백 호출
        // 카메라 프로바이더 구성, 카메라 사용 사례를 바인딩하는 역할
        cameraProviderFuture.addListener(//addListener : 비동기 작업
            {
                // cameraProviderFuture에서 얻은 CameraProvider를 cameraProvider에 할당
                cameraProvider = cameraProviderFuture.get()

                // 카메라 사용 사례를 설정
                // 카메라 미리보기, 이미지 캡쳐 및 다른 사용 사례를 설정하고 연결하는 작업을 수행
                bindCameraUseCases()
            }, ContextCompat.getMainExecutor(requireContext())
            //콜백을 메인 스레드에서 실행하기 위해 ContextCompat를 사용하여 메인 Executor가져옴
            //Android에서 UI업데이트 및 UI관련 작업은 주로 메인 스레드에서 처리되기때문
        )
    }



    /**
     * 카메라 사용 사례 설정
     * 뷰파인더에 미리보기 바인딩
     * */
    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // 카메라 선택기를 설정
        // requireLensFacing(cameraFacing)을 사용하여 카메라 렌즈 방향을 설정, build로 선택기 생성
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(cameraFacing).build()

        // 미리보기 설정
        // 비율, 디스플레이의 회전 방향을 설정
        preview = Preview.Builder()
//            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .setTargetAspectRatio(1080 * 2400)
            .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)
            .build()

        val customResolution = Size(2560, 1600) // 원하는 해상도로 설정

        // 이미지 분석 사용 설정
        // 카메라에서 스트리밍되는 영상에서 이미지 분석을 수행하는 부분
        imageAnalyzer = //이미지 분석기 설정
            ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9)    //분석기가 사용할 이미지의 종횡비 설정
                .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)   //이미지의 회전 방향 설정
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)   //이미지 처리 속도가 분석보다 빠를경우 최신 이미지만 유지
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)  //출력 이미지 형식
                .build()    // 설정한 내용으로 이미지 분석기 생성
                // 이미지 분석기의 설정 마무리 : 이미지 객체에 대한 설정작업을 진행하는 클로저 실행
                .also {
                    it.setAnalyzer(backgroundExecutor) { image ->   // 이미지 분석기에 분석할 작업 설정 -> 이미지 분석기가 카메라에서 받아온 이미지를 처리하는 부분
                        // 한번에 하면 java.lang.IllegalStateException: Image is already closed이런 오류 나옴
                        // ImageProxy를 직접 생성하는 것은 안됨
                        detectPose(image)
                        image.close()
                    }
                }

        // 이전에 설정된 카메라 사용 사례 해제
        cameraProvider.unbindAll()

        //카메라 사용 사례 바인딩
        try {
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalyzer
            )

            // 미리보기를 viewFinder에 연결
            preview?.setSurfaceProvider(fragmentCameraBinding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }


    /**
     * 이미지 프록시를 사용하여 라이브 스트리밍 중 포즈 감지를 수행함
     * 이미지에서 포즈를 감지하는 메서드
     * PoseLandmarkerHelper를 사용하여 포즈 감지
     * */
    private fun detectPose(imageProxy: ImageProxy) {
        //poseLandmarkerHelper 객체가 초기화 되었는지 확인
        if(this::poseLandmarkerHelper.isInitialized) {
            poseLandmarkerHelper.detectLiveStream(  //라이브 스트리밍 이미지에서 포즈 감지
                imageProxy = imageProxy,    // 카메라에서 가져온 이미지 데이터를 포함하는 객체 -> 포즈 감지를 위해 분석
                isFrontCamera = cameraFacing == CameraSelector.LENS_FACING_FRONT
            )
        }
    }

    /** SoundPool설정 */
    private fun setSound(){
        val context = context
        if(context != null) {
            // SoundPool 초기화
            // AudioAttributes는 오디오 재생에 대한 속성 정의하는데 사용
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA) //미디어 재생을 위한 것, 오디오 시스템에 어떤 유형의 오디오 스트림을 사용할지 알려줌
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION) // 효과음 또는 알림음과 같은 음향효과를 나타내는 것을 나타냄
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(100) // 최대 동시 재생 스트림 수 (조절 가능)
                .setAudioAttributes(audioAttributes)
                .build()

            // 사운드 파일 미리 로드
            soundMap["bass"] = soundPool.load(context, R.raw.bass, 1)
            soundMap["snare"] = soundPool.load(context, R.raw.snare, 1)
            soundMap["openHat"] = soundPool.load(context, R.raw.open_hat, 1)
            soundMap["closedHat"] = soundPool.load(context, R.raw.closed_hat, 1)
            soundMap["pedalHat"] = soundPool.load(context, R.raw.pedal_hat, 1)
            soundMap["crash"] = soundPool.load(context, R.raw.crash, 1)
            soundMap["ride"] = soundPool.load(context, R.raw.ride, 1)
            soundMap["hTom"] = soundPool.load(context, R.raw.high_tom, 1)
            soundMap["mTom"] = soundPool.load(context, R.raw.mid_tom, 1)
            soundMap["floorTom"] = soundPool.load(context, R.raw.floor_tom, 1)
        }
    }

    /** 처음 손의 위치 setting */
    private fun settingEstimation(landmarkList : com.google.mediapipe.tasks.components.containers.NormalizedLandmark) : MutableMap<String, Boolean>{

        val updates = mutableMapOf(
            "crash" to false,
            "ride" to false,
            "hiHat" to false,
            "hTom" to false,
            "mTom" to false,
            "floorTom" to false,
            "snare" to false
        )

        if(landmarkList.y() > 0.2){
            updates["crash"] = true
        }else if(landmarkList.y() > 0.35){
            updates["crash"] = true
            updates["ride"] = true
            updates["hiHat"] = true
            updates["hTom"] = true
            updates["mTom"] = true
        }else if(landmarkList.y() > 0.5){
            updates["crash"] = true
            updates["ride"] = true
            updates["hiHat"] = true
            updates["hTom"] = true
            updates["mTom"] = true
            updates["floorTom"] = true
            updates["snare"] = true
        }

        return updates
    }

    /** 처음 왼발(Hihat)의 위치 setting */
    private fun settingLeftHihat(leftFoot : com.google.mediapipe.tasks.components.containers.NormalizedLandmark){
        if(leftFoot.y() > 0.88)
            leftHihat = true
    }

    /** 처음 오른발(Bass)의 위치 setting */
    private fun settingRightBass(rightFoot : com.google.mediapipe.tasks.components.containers.NormalizedLandmark){
        if(rightFoot.y() > 0.88)
            rightBass = true
    }


    /** hit판단 */
    private fun hit(landmarkList : com.google.mediapipe.tasks.components.containers.NormalizedLandmark, hitEstimation : MutableMap<String, Boolean>) : MutableMap<String, Boolean>{
        if(landmarkList.y() > 0.2) {
            if(hitEstimation["crash"] == false && landmarkList.x() > 0.1 && landmarkList.x() < 0.35){
                Log.d("Crash","Crash Hit")
                // 사운드 재생
                val soundId = soundMap["crash"]
                soundId?.let {
                    soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
            hitEstimation["crash"] = true
        }

        if(landmarkList.y() > 0.35) {
            if(hitEstimation["hiHat"] == false && landmarkList.x() > 0 && landmarkList.x() < 0.3){
                Log.d("openHat Hit","openHat Hit")
                // 사운드 재생
                val soundId = soundMap["openHat"]
                soundId?.let {
                    soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
            if(hitEstimation["hTom"] == false && landmarkList.x() > 0.3 && landmarkList.x() < 0.47){
                Log.d("hTom Hit","hTom Hit")
                // 사운드 재생
                val soundId = soundMap["hTom"]
                soundId?.let {
                    soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
            if(hitEstimation["mTom"] == false && landmarkList.x() > 0.5 && landmarkList.x() < 0.7){
                Log.d("mTom Hit","mTom Hit")
                // 사운드 재생
                val soundId = soundMap["mTom"]
                soundId?.let {
                    soundPool.play(it, 0.8f, 0.7f, 1, 0, 1.0f)
                }
            }
            if(hitEstimation["ride"] == false && landmarkList.x() > 0.8 && landmarkList.x() < 1){
                Log.d("ride Hit","ride Hit")
                // 사운드 재생
                val soundId = soundMap["ride"]
                soundId?.let {
                    soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
            hitEstimation["hiHat"] = true
            hitEstimation["hTom"] = true
            hitEstimation["mTom"] = true
            hitEstimation["ride"] = true
        }


        if(landmarkList.y() > 0.5){
            if(hitEstimation["snare"] == false && landmarkList.x() > 0.1 && landmarkList.x() < 0.5){
                Log.d("snare Hit","snare Hit")
                // 사운드 재생
                val soundId = soundMap["snare"]
                soundId?.let {
                    soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
            if(hitEstimation["floorTom"] == false && landmarkList.x() > 0.7 && landmarkList.x() < 0.9){
                Log.d("floorTom Hit","floorTom Hit")
                // 사운드 재생
                val soundId = soundMap["floorTom"]
                soundId?.let {
                    soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
            hitEstimation["snare"] = true
            hitEstimation["floorTom"] = true
        }

        return hitEstimation
    }

    private fun hitLeftHihat(leftFoot : com.google.mediapipe.tasks.components.containers.NormalizedLandmark){
        if(leftHihat == false && leftFoot.y() > 0.88 && leftFoot.x() > 0.6 && leftFoot.x() < 0.8){

            Log.d("[Foot] bass hit!","[Foot] bass hit! ${leftFoot.y()}")
            val soundId = soundMap["bass"]
            soundId?.let {
                soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
            }
            leftHihat = true
        }
    }
    private fun hitRightBass(rightFoot : com.google.mediapipe.tasks.components.containers.NormalizedLandmark){
        if(rightBass == false && rightFoot.y() > 0.88 && rightFoot.x() > 0.2 && rightFoot.x() < 0.45){

            Log.d("[Foot] pedalHat hit!","[Foot] pedalHat hit! ${rightFoot.y()}")
            val soundId = soundMap["pedalHat"]

            soundId?.let {
                soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
            }
            rightBass = true
        }
    }

    /** hit소리를 낼 준비하는지 판단 */
    private fun back(landmarkList : com.google.mediapipe.tasks.components.containers.NormalizedLandmark, hitEstimation : MutableMap<String, Boolean>) : MutableMap<String, Boolean>{
        if(landmarkList.y() < 0.19) {
            hitEstimation["crash"] = false
            hitEstimation["ride"] = false
            hitEstimation["hiHat"] = false
            hitEstimation["hTom"] = false
            hitEstimation["mTom"] = false
            hitEstimation["floorTom"] = false
            hitEstimation["snare"] = false
        }
        if(landmarkList.y() < 0.34) {
            hitEstimation["ride"] = false
            hitEstimation["hiHat"] = false
            hitEstimation["hTom"] = false
            hitEstimation["mTom"] = false
            hitEstimation["floorTom"] = false
            hitEstimation["snare"] = false
        }
        if(landmarkList.y() < 0.49){
            hitEstimation["floorTom"] = false
            hitEstimation["snare"] = false
        }

        return hitEstimation
    }
    private fun backLeftHihat(leftFoot : com.google.mediapipe.tasks.components.containers.NormalizedLandmark){
        if(leftFoot.y() < 0.88){
            leftHihat = false
        }
    }
    private fun backRightBass(rightFoot : com.google.mediapipe.tasks.components.containers.NormalizedLandmark){
        if(rightFoot.y() < 0.88){
            rightBass = false
        }
    }

    /**
     * 포즈 감지 결과를 처리하고 UI를 업데이트
     * PoseLandmarkerHelper.LandmarkerListener의 onResults함수 재정의
     * */
    override fun onResults(
        resultBundle: PoseLandmarkerHelper.ResultBundle
    ) {
        // 포즈 감지 결과를 화면에 표시 -> UI스레드에서 작업을 수행
        activity?.runOnUiThread {
            if (_fragmentCameraBinding != null) {
                // fragmentCameraBinding.overlay - 화면에 그리기 작업을 처리하는 커스텀 OverlayView
                fragmentCameraBinding.overlay.setResults(
                    resultBundle.results.first(),   // 포즈 감지 결과 중 첫번째 포즈
                    // 입력 이미지의 높이, 너비
                    resultBundle.inputImageHeight,
                    resultBundle.inputImageWidth,
                    //현재 실행 중인 모드
                    RunningMode.LIVE_STREAM
                )
                var pose = resultBundle.results.get(0)
                if(pose.landmarks().size > 0){
                    if(pose.landmarks().get(0).size > 32){

                        var leftHand = pose.landmarks().get(0).get(17)
                        var rightHand = pose.landmarks().get(0).get(18)
                        var leftFoot = pose.landmarks().get(0).get(31)
                        var rightFoot = pose.landmarks().get(0).get(32)

                        if(!start){
                            leftHandEstimation = settingEstimation(leftHand)
                            rightHandEstimation = settingEstimation(rightHand)
                            settingLeftHihat(leftFoot)
                            settingRightBass(rightFoot)
                            start = true
                        }


                        //hit
                        //foot
                        hitLeftHihat(leftFoot)
                        hitRightBass(rightFoot)
                        //hands
                        leftHandEstimation = hit(leftHand,leftHandEstimation)
                        rightHandEstimation = hit(rightHand,rightHandEstimation)


                        //back
                        //foot
                        backLeftHihat(leftFoot)
                        backRightBass(rightFoot)
                        //hands
                        leftHandEstimation = back(leftHand,leftHandEstimation)
                        rightHandEstimation = back(rightHand,rightHandEstimation)
                    }
                }

                // overlayView를 화면에 다시 그리도록 invalidate메서드 호출
                // -> 포즈 감지 결과가 화면에 업데이트 및 표시됨
                fragmentCameraBinding.overlay.invalidate()
            }
        }
    }

    /**
     * 오류 발생시 호출
     * 오류 메시지 표시 -> 필요한 조치
     * */
    override fun onError(error: String, errorCode: Int) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }
}
