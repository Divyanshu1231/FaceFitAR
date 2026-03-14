package com.example.facefitar.filters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import kotlin.math.abs
import kotlin.math.atan2

class FilterOverlayView(
    context: Context,
    attrs: AttributeSet?
) : View(context, attrs) {


    private val paint = Paint()

    private var faces: List<Face> = emptyList()
    private var filterBitmap: Bitmap? = null
    private var filterType: FilterType = FilterType.GLASSES

    private var imageWidth = 0
    private var imageHeight = 0


    fun setFilter(bitmap: Bitmap, type: FilterType) {

        filterBitmap = bitmap
        filterType = type
        invalidate()
    }


    fun updateFaces(faceList: List<Face>, width: Int, height: Int) {

        faces = faceList
        imageWidth = width
        imageHeight = height

        invalidate()
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        val bitmap = filterBitmap ?: return
        if (imageWidth == 0 || imageHeight == 0) return

        val scaleX = width.toFloat() / imageWidth
        val scaleY = height.toFloat() / imageHeight

        for (face in faces) {

            val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
            val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)

            if (leftEye != null && rightEye != null) {

                val left = leftEye.position
                val right = rightEye.position

                var leftX = left.x * scaleX
                var leftY = left.y * scaleY

                var rightX = right.x * scaleX
                var rightY = right.y * scaleY

                // mirror for front camera
                leftX = width - leftX
                rightX = width - rightX

                val centerX = (leftX + rightX) / 2f
                val centerY = (leftY + rightY) / 2f

                val eyeDistance = abs(leftX - rightX)

                val angle = Math.toDegrees(
                    atan2(
                        (rightY - leftY).toDouble(),
                        (rightX - leftX).toDouble()
                    )
                ).toFloat()

                canvas.save()
                canvas.rotate(angle, centerX, centerY)

                val rect = when (filterType) {

                    FilterType.GLASSES -> {

                        val width = eyeDistance * 2.4f
                        val height = width * bitmap.height / bitmap.width

                        RectF(
                            centerX - width / 2,
                            centerY - height / 2,
                            centerX + width / 2,
                            centerY + height / 2
                        )
                    }

                    FilterType.CROWN -> {

                        val width = eyeDistance * 3f
                        val height = width * bitmap.height / bitmap.width

                        RectF(
                            centerX - width / 2,
                            centerY - height * 1.8f,
                            centerX + width / 2,
                            centerY - height * 0.8f
                        )
                    }

                    FilterType.EARS -> {

                        val width = eyeDistance * 3f
                        val height = width * bitmap.height / bitmap.width

                        RectF(
                            centerX - width / 2,
                            centerY - height * 2.2f,
                            centerX + width / 2,
                            centerY - height * 1.2f
                        )
                    }

                    FilterType.MASK -> {

                        val width = eyeDistance * 2.2f
                        val height = width * bitmap.height / bitmap.width

                        RectF(
                            centerX - width / 2,
                            centerY - height * 0.3f,
                            centerX + width / 2,
                            centerY + height * 0.7f
                        )
                    }

                    FilterType.DECORATION -> {

                        val width = eyeDistance * 3f
                        val height = width * bitmap.height / bitmap.width

                        RectF(
                            centerX - width / 2,
                            centerY - height * 3f,
                            centerX + width / 2,
                            centerY - height * 2f
                        )
                    }
                }

                canvas.drawBitmap(bitmap, null, rect, paint)

                canvas.restore()
            }
        }
    }


}
