package com.example.mobile2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mobile2.data.FoodItem
import com.example.mobile2.QrAnalyzer
import com.example.mobile2.util.BottomNavUtil
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private var isDialogShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        BottomNavUtil.setup(this, R.id.menu_qr)

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (hasCameraPermission()) {
            startCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            1001
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(
                    findViewById<androidx.camera.view.PreviewView>(R.id.previewView)
                        .surfaceProvider
                )
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor,
                        QrAnalyzer { qrText ->
                            Log.d("QR_TEST", "QR 인식됨: $qrText") // ✅ 로그
                            runOnUiThread {
                                showFoodItemPopup(qrText)
                            }
                        }
                    )
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    // 🔥 QR 결과 팝업
    private fun showFoodItemPopup(qrText: String) {
        if (isDialogShowing) return

        val foodId = qrText.toIntOrNull() ?: return
        isDialogShowing = true

        val foodItem = FoodItem(
            id = foodId,
            name = "우유",
            category = "유제품",
            expireDate = "2026-02-01"
        )

        AlertDialog.Builder(this)
            .setTitle("재료 정보")
            .setMessage(
                """
                ID: ${foodItem.id}
                이름: ${foodItem.name}
                카테고리: ${foodItem.category}
                유통기한: ${foodItem.expireDate}
                """.trimIndent()
            )
            .setPositiveButton("확인") { dialog, _ ->
                isDialogShowing = false
                dialog.dismiss()
            }
            .setOnDismissListener {
                isDialogShowing = false
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
