package com.example.mobile2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile2.util.BottomNavUtil

class MyPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        BottomNavUtil.setup(this, R.id.menu_mypage)

        // 개인정보 수정 클릭
        val tvEditProfile = findViewById<TextView>(R.id.tvEditProfile)
        tvEditProfile.setOnClickListener {
            val intent = Intent(this, EditPageActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.tvLogout).setOnClickListener {
            showLogoutDialog()
        }

        findViewById<TextView>(R.id.tvShoppingLink).setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.kurly.com/main")
            )
            startActivity(intent)
        }

        findViewById<TextView>(R.id.tvMonthlyReport).setOnClickListener {
            startActivity(Intent(this, MonthlyReportActivity::class.java))
        }
    }

    // 로그아웃
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setMessage("로그아웃 하시겠습니까?")
            .setPositiveButton("확인") { _, _ ->
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
            .setNegativeButton("취소", null)
            .show()
    }
}

