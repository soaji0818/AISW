package com.example.mobile2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile2.util.BottomNavUtil

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        BottomNavUtil.setup(this, R.id.menu_home)

        // 냉장고 이동
        findViewById<TextView>(R.id.tvArrowBalance).setOnClickListener {
            startActivity(Intent(this, FridgeActivity::class.java))
        }

        // 알림 요약
        findViewById<View>(R.id.cardAlarmSummary).setOnClickListener {
            startActivity(Intent(this, AlarmActivity::class.java))
        }

        //  레시피 검색하기 → 레시피 추천
        findViewById<View>(R.id.layoutRecipeSearch).setOnClickListener {
            startActivity(Intent(this, RecipeActivity::class.java))
        }
    }
}
