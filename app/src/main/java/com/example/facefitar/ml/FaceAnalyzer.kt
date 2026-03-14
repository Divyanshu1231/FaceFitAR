package com.example.facefitar.ml

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*

class FaceAnalyzer(
    private val listener: (List<Face>, Int, Int) -> Unit
) : ImageAnalysis.Analyzer {

    private val detector: FaceDetector

    init {

        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .enableTracking()
            .build()

        detector = FaceDetection.getClient(options)
    }

    override fun analyze(imageProxy: ImageProxy) {

        val mediaImage = imageProxy.image

        if (mediaImage != null) {

            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            detector.process(image)

                .addOnSuccessListener { faces ->

                    listener(
                        faces,
                        image.width,
                        image.height
                    )
                }

                .addOnFailureListener {
                    // log if needed
                }

                .addOnCompleteListener {
                    imageProxy.close()
                }

        } else {
            imageProxy.close()
        }
    }

}
