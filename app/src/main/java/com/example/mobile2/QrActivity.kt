package com.example.mobile2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mobile2.api.Api
import com.example.mobile2.data.FoodItem
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
                            Log.d("QR_TEST", "QR 인식됨: $qrText")
                            runOnUiThread {
                                showFoodItemPopup(qrText)
                            }
                        }
                    )
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    //  QR 결과 → 제품 정보 팝업
    private fun showFoodItemPopup(scannedText: String) {
        if (isDialogShowing) return

        val foodId = scannedText.toIntOrNull() ?: return
        isDialogShowing = true

        Thread {
            try {
                val ingredients = Api.getIngredients()
                val foodItem = ingredients.find { it.id == foodId }

                if (foodItem == null) {
                    runOnUiThread { isDialogShowing = false }
                    return@Thread
                }

                runOnUiThread {
                    showFoodDialog(foodItem)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { isDialogShowing = false }
            }
        }.start()
    }

    //  제품 정보 다이얼로그
    private fun showFoodDialog(foodItem: FoodItem) {
        val view = layoutInflater.inflate(R.layout.dialog_qr, null)

        view.findViewById<TextView>(R.id.tvName).text =
            "제품명: ${foodItem.name}"

        view.findViewById<TextView>(R.id.tvCategory).text =
            "카테고리: ${foodItem.category}"

        view.findViewById<TextView>(R.id.tvExpireDate).text =
            "유통기한: ${foodItem.expiryDate}"

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(true)
            .create()

        //  닫기 버튼
        view.findViewById<TextView>(R.id.btnClose).setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnShowListener {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        dialog.setOnDismissListener {
            isDialogShowing = false
        }

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
