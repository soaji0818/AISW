package com.example.mobile2

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 루트 레이아웃
        val root = findViewById<View>(R.id.splashRoot)

        // 블루 그라데이션 (위 → 아래)
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor("#0F2A44"), // 네이비
                Color.parseColor("#1F6AE1"), // 메인 블루
                Color.parseColor("#60A5FA")  // 라이트 블루
            )
        )

        root.background = gradient

        // 2초 후 메인으로 이동
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }
}
