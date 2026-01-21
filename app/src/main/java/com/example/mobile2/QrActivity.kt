package com.example.mobile2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mobile2.util.BottomNavUtil
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.example.mobile2.api.Api

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
                            Log.d("QR_TEST", "QR 인식됨: $qrText") //
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

    // QR 결과 팝업
    private fun showFoodItemPopup(scannedText: String) {
        if (isDialogShowing) return

        val foodId = scannedText.toIntOrNull() ?: return
        isDialogShowing = true

        Thread {
            try {
                val ids = Api.getIngredientIds()

                runOnUiThread {
                    if (!ids.contains(foodId)) {
                        isDialogShowing = false
                        return@runOnUiThread
                    }

                    val dialogView = layoutInflater.inflate(R.layout.dialog_qr, null)
                    val ivQr = dialogView.findViewById<ImageView>(R.id.ivQr)
                    ivQr.setImageBitmap(QrUtil.makeQrBitmap(foodId.toString()))

                    AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setOnDismissListener {
                            isDialogShowing = false
                        }
                        .show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { isDialogShowing = false }
            }
        }.start()
    }





    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
