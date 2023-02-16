package com.example.mlkit_method_channel_test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import kotlin.math.abs


class MainActivity: FlutterFragmentActivity() {

    var isSuccess: Boolean = false

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "com.example.method_channel").setMethodCallHandler { call, result ->
            if (call.method == "check") {
               val imageParam =  call.argument<String>("image")
                val bmpImage = getBitmapFromBase64String(imageParam!!)
                detectFaces(InputImage.fromBitmap(bmpImage, 0),result)
            } else {
                result.notImplemented()
            }
        }
    }

    private fun getBitmapFromBase64String(base64String: String): Bitmap {
        val decodedString: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }


    private fun detectFaces(image: InputImage, methodChannelResult: MethodChannel.Result) {
        isSuccess = false
        // [START set_detector_options]
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()
        // [END set_detector_options]

        // [START get_detector]
        val detector = FaceDetection.getClient(options)
        // Or, to use the default option:
        // val detector = FaceDetection.getClient();
        // [END get_detector]

        // [START run_detector]
        val result = detector.process(image)
            .addOnSuccessListener { faces ->
                // Task completed successfully
                // [START_EXCLUDE]
                // [START get_face_info]
                for (face in faces) {
                    val bounds = face.boundingBox
                    val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                    val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                    // nose available):
                    val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                    val rightEar = face.getLandmark(FaceLandmark.RIGHT_EAR)


                    Log.d("SHADIK", "X Axis Value ${face.headEulerAngleX}")
                    Log.d("SHADIK", "Y Axis Value ${face.headEulerAngleY}")
                    Log.d("SHADIK", "Z Axis Value ${face.headEulerAngleZ}")

                     var leftEarPos: Float ?= null
                     var rightEarPos: Float ?= null
                    leftEar?.let {
                        leftEarPos = leftEar.position.y
                        Log.d("SHADIK", leftEarPos.toString())
                    }
                    rightEar?.let {
                        rightEarPos = rightEar.position.y
                        Log.d("SHADIK", rightEarPos.toString())
                    }
                    if(face.rightEyeOpenProbability != null &&
                        face.leftEyeOpenProbability != null &&
                        leftEarPos != null &&
                        rightEarPos != null &&
                        abs(leftEarPos!! - rightEarPos!!) <=10
                        ) {
                        Log.d("SHADIK", face.rightEyeOpenProbability.toString())
                        Log.d("SHADIK", face.leftEyeOpenProbability.toString())
                        isSuccess = (face.rightEyeOpenProbability!! > 0.98 &&
                                face.leftEyeOpenProbability!! > 0.98 &&
                                face.headEulerAngleY>=-2 &&
                                face.headEulerAngleY <= 2)
                    }

                }
            }
            .addOnFailureListener {
                Log.d("SHADIK", "FACE NAI")
                isSuccess = false
            }
            .addOnCompleteListener {
                Log.d("SHADIK", "COMPLETE")
                Log.d("SHADIK", "COM : ${isSuccess.toString()}")
                methodChannelResult.success(isSuccess.toString())
            }
        Log.d("SHADIK","result ${result.toString()}")
    }
}
