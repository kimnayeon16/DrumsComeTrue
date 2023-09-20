/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ssafy.drumscometrue.freePlay

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult

class HandLandmarkerHelper(
    var minHandDetectionConfidence: Float = DEFAULT_HAND_DETECTION_CONFIDENCE,
    var minHandTrackingConfidence: Float = DEFAULT_HAND_TRACKING_CONFIDENCE,
    var minHandPresenceConfidence: Float = DEFAULT_HAND_PRESENCE_CONFIDENCE,
    var maxNumHands: Int = DEFAULT_NUM_HANDS,
    var currentDelegate: Int = DELEGATE_GPU,
    var runningMode: RunningMode = RunningMode.IMAGE,
    val context: Context,
    // 이 리스너는 RunningMode.LIVE_STREAM 모드에서만 사용됩니다.
    val handLandmarkerHelperListener: LandmarkerListener? = null
) {

    // 이 예제에서는 설정이 변경될 때마다 재설정할 수 있도록 var 여야 합니다.
    // Hand Landmarker가 변경되지 않는 경우, lazy val을 사용하는 것이 좋습니다.
//    private var handLandmarker: HandLandmarker? = null
    var handLandmarker: HandLandmarker? = null

    init {
        setupHandLandmarker()
    }

    fun clearHandLandmarker() {
        handLandmarker?.close()
        handLandmarker = null
    }

    // HandLandmarkerHelper의 실행 상태를 반환합니다.
    fun isClose(): Boolean {
        return handLandmarker == null
    }


    // 현재 설정을 사용하여 Hand Landmarker를 초기화합니다.
    // CPU는 메인 스레드에서 만들어지고 백그라운드 스레드에서 사용되는 Landmarker와 함께 사용할 수 있지만 GPU 대리자는 Landmarker를 초기화한 스레드에서 사용해야 합니다.
    // Landmarker
    fun setupHandLandmarker() {
        // 일반적인 Hand Landmarker 옵션을 설정합니다.
        val baseOptionBuilder = BaseOptions.builder()

        // 모델을 실행하는 데 사용할 하드웨어를 설정합니다. 기본값은 GPU입니다.
        when (currentDelegate) {
            DELEGATE_CPU -> {
                baseOptionBuilder.setDelegate(Delegate.CPU)
            }
            DELEGATE_GPU -> {
                baseOptionBuilder.setDelegate(Delegate.GPU)
            }
        }

        baseOptionBuilder.setModelAssetPath(MP_HAND_LANDMARKER_TASK)

        // runningMode가 handLandmarkerHelperListener와 일치하는지 확인합니다.
        when (runningMode) {
            RunningMode.LIVE_STREAM -> {
                if (handLandmarkerHelperListener == null) {
                    throw IllegalStateException(
                        "runningMode가 LIVE_STREAM일 때 handLandmarkerHelperListener를 설정해야 합니다."
                    )
                }
            }
            else -> {
                // no-op
            }
        }

        try {
            val baseOptions = baseOptionBuilder.build()
            // Hand Landmarker만을 위해 사용되는 일반 옵션과 특별한 옵션을 설정합니다.
            // options only use for Hand Landmarker.
            val optionsBuilder =
                HandLandmarker.HandLandmarkerOptions.builder()
                    .setBaseOptions(baseOptions)
                    .setMinHandDetectionConfidence(minHandDetectionConfidence)
                    .setMinTrackingConfidence(minHandTrackingConfidence)
                    .setMinHandPresenceConfidence(minHandPresenceConfidence)
                    .setNumHands(maxNumHands)
                    .setRunningMode(runningMode)

            // LIVE_STREAM 모드에서만 사용되는 ResultListener 및 ErrorListener입니다.
            if (runningMode == RunningMode.LIVE_STREAM) {
                optionsBuilder
                    .setResultListener(this::returnLivestreamResult)
                    .setErrorListener(this::returnLivestreamError)
            }

            val options = optionsBuilder.build()
            handLandmarker =
                HandLandmarker.createFromOptions(context, options)
        } catch (e: IllegalStateException) {
            handLandmarkerHelperListener?.onError(0,
                "Hand Landmarker failed to initialize. See error logs for " +
                        "details"
            )
            Log.e(
                TAG, "MediaPipe failed to load the task with error: " + e
                    .message
            )
        } catch (e: RuntimeException) {
            // This occurs if the model being used does not support GPU
            handLandmarkerHelperListener?.onError(0,
                "Hand Landmarker failed to initialize. See error logs for " +
                        "details", GPU_ERROR
            )
            Log.e(
                TAG,
                "Image classifier failed to load model with error: " + e.message
            )
        }
    }

    // ImageProxy를 MPImage로 변환하고 HandlandmarkerHelper에 전달합니다.
    fun detectLiveStream(
        imageProxy: ImageProxy,
        isFrontCamera: Boolean
    ) {
        if (runningMode != RunningMode.LIVE_STREAM) {
            throw IllegalArgumentException(
                "Attempting to call detectLiveStream" +
                        " while not using RunningMode.LIVE_STREAM"
            )
        }
        val frameTime = SystemClock.uptimeMillis()

        // 프레임에서 RGB 비트를 비트맵 버퍼로 복사합니다.
        val bitmapBuffer =
            Bitmap.createBitmap(
                imageProxy.width,
                imageProxy.height,
                Bitmap.Config.ARGB_8888
            )
//        imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }
//        imageProxy.close()
//        println("handHelper1"+imageProxy.planes[0].buffer)
        bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer)
        // 여기서 imageProxy를 사용하여 pos값이 변경됨 -> 다른 곳에서 사용하기 위해 초기화 함
        imageProxy.planes[0].buffer.position(0)
//        println("handHelper2"+imageProxy.planes[0].buffer)

        val matrix = Matrix().apply {
            // 카메라에서 받은 프레임을 표시될 방향으로 회전시킵니다.
            postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())

            // 사용자가 프론트 카메라를 사용하는 경우 이미지를 뒤집습니다.
            if (isFrontCamera) {
                postScale(
                    -1f,
                    1f,
                    imageProxy.width.toFloat(),
                    imageProxy.height.toFloat()
                )
            }
        }
        val rotatedBitmap = Bitmap.createBitmap(
            bitmapBuffer, 0, 0, bitmapBuffer.width, bitmapBuffer.height,
            matrix, true
        )

        // 입력 Bitmap 개체를 실행하여 MPImage 개체로 변환하여 추론을 실행합니다.
        val mpImage = BitmapImageBuilder(rotatedBitmap).build()

        detectAsync(mpImage, frameTime)
    }

    // MediaPipe Hand Landmarker API를 사용하여 비동기로 Hand Landmark를 실행합니다.
    @VisibleForTesting
    fun detectAsync(mpImage: MPImage, frameTime: Long) {
        handLandmarker?.detectAsync(mpImage, frameTime)
        // As we're using running mode LIVE_STREAM, the landmark result will
        // be returned in returnLivestreamResult function
    }

    // 사용자 갤러리에서로드 한 비디오 파일의 URI를 허용하고 Hand Landmarker 추론을 실행합니다.
    // 이 프로세스는 비디오의 모든 프레임을 평가하고 결과를 반환할 번들에 첨부합니다.
    // returned.
    fun detectVideoFile(
        videoUri: Uri,
        inferenceIntervalMs: Long
    ): ResultBundle? {
        if (runningMode != RunningMode.VIDEO) {
            throw IllegalArgumentException(
                "Attempting to call detectVideoFile" +
                        " while not using RunningMode.VIDEO"
            )
        }

        // 추론 시간은 프로세스의 시작 및 종료에서 시스템 시간의 차이입니다.
        // process
        val startTime = SystemClock.uptimeMillis()

        var didErrorOccurred = false

        // 비디오에서 프레임을 로드하고 Hand Landmarker를 실행합니다.
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, videoUri)
        val videoLengthMs =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLong()

        // 주의: 미디어 리트리버는 실제 비디오 파일의 크기보다 작은 프레임을 반환하므로,
        // 비디오의 실제 차원을 얻지 못할 수 있습니다.
        val firstFrame = retriever.getFrameAtTime(0)
        val width = firstFrame?.width
        val height = firstFrame?.height

        // 비디오가 유효하지 않은 경우 null 감지 결과를 반환합니다.
        if ((videoLengthMs == null) || (width == null) || (height == null)) return null

        // 다음으로, frameInterval ms마다 한 프레임을 가져와 추론을 실행합니다.
        val resultList = mutableListOf<HandLandmarkerResult>()
        var result: HandLandmarkerResult? = null
        val numberOfFrameToRead = videoLengthMs.div(inferenceIntervalMs)

        for (i in 0..numberOfFrameToRead) {
            val timestampMs = i * inferenceIntervalMs // ms

            retriever
                .getFrameAtTime(
                    timestampMs * 1000, // convert from ms to micro-s
                    MediaMetadataRetriever.OPTION_CLOSEST
                )
                ?.let { frame ->
                    // 비디오 프레임을 MediaPipe에서 필요한 ARGB_8888로 변환합니다.
                    val argb8888Frame =
                        if (frame.config == Bitmap.Config.ARGB_8888) frame
                        else frame.copy(Bitmap.Config.ARGB_8888, false)

                    // 입력 Bitmap 개체를 실행하여 MPImage 개체로 변환하여 추론을 실행합니다.
                    val mpImage = BitmapImageBuilder(argb8888Frame).build()

                    // MediaPipe Hand Landmarker API를 사용하여 Hand Landmarker를 실행합니다.
                    handLandmarker?.detectForVideo(mpImage, timestampMs)
                        ?.let { detectionResult ->
                            resultList.add(detectionResult)
                            result = detectionResult
                        } ?: {
                        didErrorOccurred = true
                        handLandmarkerHelperListener?.onError(0,
                            "ResultBundle could not be returned" +
                                    " in detectVideoFile"
                        )
                    }
                }
                ?: run {
                    didErrorOccurred = true
                    handLandmarkerHelperListener?.onError(0,
                        "Frame at specified time could not be" +
                                " retrieved when detecting in video."
                    )
                }
        }

        retriever.release()

        val inferenceTimePerFrameMs =
            (SystemClock.uptimeMillis() - startTime).div(numberOfFrameToRead)

        return if (didErrorOccurred) {
            null
        } else {
            ResultBundle(resultList, result, inferenceTimePerFrameMs, height, width)
        }
    }

    // 받은 Bitmap에서 Hand Landmarker 추론을 실행하고 결과를 반환합니다.
    // results back to the caller
    fun detectImage(image: Bitmap): ResultBundle? {
        if (runningMode != RunningMode.IMAGE) {
            throw IllegalArgumentException(
                "Attempting to call detectImage" +
                        " while not using RunningMode.IMAGE"
            )
        }


        // 추론 시간은 프로세스의 시작 및 종료에서 시스템 시간의 차이입니다.
        // start and finish of the process
        val startTime = SystemClock.uptimeMillis()

        // 입력 Bitmap 개체를 실행하여 MPImage 개체로 변환하여 추론을 실행합니다.
        val mpImage = BitmapImageBuilder(image).build()

        // MediaPipe Hand Landmarker API를 사용하여 Hand Landmarker를 실행합니다.
        handLandmarker?.detect(mpImage)?.also { landmarkResult ->
            val inferenceTimeMs = SystemClock.uptimeMillis() - startTime
            return ResultBundle(
                listOf(landmarkResult),
                landmarkResult,
                inferenceTimeMs,
                image.height,
                image.width
            )
        }

        // handLandmarker?.detect()가 null을 반환하면 오류입니다. 이를 나타내기 위해 null을 반환합니다.
        // to indicate this.
        handLandmarkerHelperListener?.onError(0,
            "Hand Landmarker failed to detect."
        )
        return null
    }

    // HandLandmarkerHelper의 호출자에게 landmark 결과를 반환합니다.
    private fun returnLivestreamResult(
        result: HandLandmarkerResult,
        input: MPImage
    ) {
        val finishTimeMs = SystemClock.uptimeMillis()
        val inferenceTime = finishTimeMs - result.timestampMs()

        handLandmarkerHelperListener?.onResults(
            ResultBundle(
                listOf(result),
                result,
                inferenceTime,
                input.height,
                input.width
            )
        )
    }

    // 감지 중에 발생한 오류를 이 HandLandmarkerHelper의 호출자에게 반환합니다.
    // caller
    private fun returnLivestreamError(error: RuntimeException) {
        handLandmarkerHelperListener?.onError(0,
            error.message ?: "An unknown error has occurred"
        )
    }

    companion object {
        const val TAG = "HandLandmarkerHelper"
        private const val MP_HAND_LANDMARKER_TASK = "hand_landmarker.task"

        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val DEFAULT_HAND_DETECTION_CONFIDENCE = 0.5F
        const val DEFAULT_HAND_TRACKING_CONFIDENCE = 0.5F
        const val DEFAULT_HAND_PRESENCE_CONFIDENCE = 0.5F
        const val DEFAULT_NUM_HANDS = 2
        const val OTHER_ERROR = 0
        const val GPU_ERROR = 1
    }

    data class ResultBundle(
        val results: List<HandLandmarkerResult>,    //한개의 손만을 가짐
        val result: HandLandmarkerResult?,
        val inferenceTime: Long,
        val inputImageHeight: Int,
        val inputImageWidth: Int,
    )

    interface LandmarkerListener {
        fun onError(hand: Int ,error: String, errorCode: Int = OTHER_ERROR)
        fun onResults(resultBundle: ResultBundle)
    }
}
