package com.example.facefitar.ui.camera

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facefitar.R
import com.example.facefitar.databinding.ActivityCameraBinding
import com.example.facefitar.filters.FilterType
import com.example.facefitar.ml.FaceAnalyzer
import com.example.facefitar.viewmodel.CameraViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCameraBinding

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private lateinit var cameraExecutor: ExecutorService

    private val viewModel: CameraViewModel by viewModels()

    private var lastMediaUri: Uri? = null

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        checkCameraPermission()
        observeFilters()
        loadLatestMedia()

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.glasses_classic)
        binding.filterOverlay.setFilter(bitmap, FilterType.GLASSES)

        binding.captureButton.setOnClickListener { capturePhoto() }

        binding.videoButton.setOnClickListener {

            if (recording == null) startRecording()
            else stopRecording()
        }

        binding.galleryPreview.setOnClickListener {

            lastMediaUri?.let {

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(it, "video/*")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            }
        }
    }

// ---------------- LOAD LAST MEDIA ----------------

    private fun loadLatestMedia() {

        val projection = arrayOf(MediaStore.Images.Media._ID)

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )

        cursor?.use {

            if (it.moveToFirst()) {

                val id =
                    it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))

                val uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )

                lastMediaUri = uri
                binding.galleryPreview.setImageURI(uri)
            }
        }
    }

// ---------------- UPDATE THUMBNAIL ----------------

    private fun updateThumbnail(uri: Uri) {

        lastMediaUri = uri
        binding.galleryPreview.setImageURI(uri)
    }

// ---------------- FILTER LIST ----------------

    private fun observeFilters() {

        viewModel.filters.observe(this) { filters ->

            val adapter = FilterAdapter(filters) {

                val bitmap = BitmapFactory.decodeResource(resources, it.overlay)

                val type = when (it.name) {

                    "Glasses" -> FilterType.GLASSES
                    "Crown" -> FilterType.CROWN
                    "Cat Ears" -> FilterType.EARS
                    "Dog Ears" -> FilterType.EARS
                    "Mask" -> FilterType.MASK
                    else -> FilterType.DECORATION
                }

                binding.filterOverlay.setFilter(bitmap, type)
            }

            binding.filterRecycler.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            binding.filterRecycler.adapter = adapter
        }
    }

// ---------------- CAMERA PERMISSION ----------------

    private fun checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            startCamera()

        } else {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {

            startCamera()

        } else {

            Toast.makeText(
                this,
                "Camera permission required",
                Toast.LENGTH_LONG
            ).show()
        }
    }

// ---------------- START CAMERA ----------------

    private fun startCamera() {

        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {

                it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(
                    ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                )
                .build()

            imageAnalyzer.setAnalyzer(
                cameraExecutor,
                FaceAnalyzer { faces, width, height ->

                    binding.filterOverlay.updateFaces(
                        faces,
                        width,
                        height
                    )
                }
            )

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HD))
                .build()

            videoCapture = VideoCapture.withOutput(recorder)

            val cameraSelector =
                CameraSelector.DEFAULT_FRONT_CAMERA

            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalyzer,
                videoCapture
            )

        }, ContextCompat.getMainExecutor(this))
    }

// ---------------- PHOTO CAPTURE ----------------

    private fun capturePhoto() {

        val cameraBitmap = binding.cameraPreview.bitmap ?: return
        val overlayBitmap = binding.filterOverlay.drawToBitmap()

        val resultBitmap =
            Bitmap.createBitmap(cameraBitmap.width, cameraBitmap.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(resultBitmap)

        canvas.drawBitmap(cameraBitmap, 0f, 0f, null)
        canvas.drawBitmap(overlayBitmap, 0f, 0f, null)

        val name = "FaceFitAR_${System.currentTimeMillis()}.jpg"

        val contentValues = ContentValues().apply {

            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FaceFitAR")
        }

        val uri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        uri?.let {

            contentResolver.openOutputStream(it)?.use { stream ->
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }

            updateThumbnail(it)

            Toast.makeText(this, "Photo saved with filter", Toast.LENGTH_SHORT).show()
        }
    }

// ---------------- START VIDEO ----------------

    private fun startRecording() {

        val videoCapture = videoCapture ?: return

        val name = "FaceFitAR_${System.currentTimeMillis()}.mp4"

        val contentValues = ContentValues().apply {

            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/FaceFitAR")
        }

        val mediaStoreOutputOptions =
            MediaStoreOutputOptions.Builder(
                contentResolver,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            )
                .setContentValues(contentValues)
                .build()

        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .start(ContextCompat.getMainExecutor(this)) { event ->

                if (event is VideoRecordEvent.Finalize) {

                    if (!event.hasError()) {

                        val uri = event.outputResults.outputUri
                        updateThumbnail(uri)

                        Toast.makeText(
                            this,
                            "Video saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    recording = null
                }
            }
    }

// ---------------- STOP VIDEO ----------------

    private fun stopRecording() {

        recording?.stop()
        recording = null
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


}
