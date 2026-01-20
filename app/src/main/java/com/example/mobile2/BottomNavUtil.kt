package com.example.mobile2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


object BottomNavUtil {

    fun setup(activity: AppCompatActivity, selectedItemId: Int) {
        val bottomNav = activity.findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == selectedItemId) return@setOnItemSelectedListener true

            val target = when (item.itemId) {
                R.id.menu_home -> HomeActivity::class.java
                R.id.menu_fridge -> FridgeActivity::class.java
                // 아래는 Activity 만들면 주석 해제해서 쓰기
                // R.id.menu_qr -> QrActivity::class.java
                // R.id.menu_recipe -> RecipeActivity::class.java
                // R.id.menu_mypage -> MyPageActivity::class.java
                else -> null
            }

            if (target != null) {
                val intent = Intent(activity, target).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                activity.startActivity(intent)
                activity.overridePendingTransition(0, 0)
                activity.finish() // 뒤로가기 누르면 탭 이동 히스토리 안 쌓이게
                true
            } else {
                false
            }
        }

        // 현재 탭 선택 표시
        bottomNav.selectedItemId = selectedItemId
    }
}
