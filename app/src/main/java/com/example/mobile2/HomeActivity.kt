package com.example.mobile2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile2.api.Api
import com.example.mobile2.data.FoodItem
import com.example.mobile2.util.BottomNavUtil
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HomeActivity : AppCompatActivity() {

    // 임박 기준 (일)
    private val SOON_DAYS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        BottomNavUtil.setup(this, R.id.menu_home)

        findViewById<View>(R.id.cardAlarmSummary).setOnClickListener {
            startActivity(Intent(this, AlarmActivity::class.java))
        }

        findViewById<View>(R.id.layoutfridge).setOnClickListener {
            startActivity(Intent(this, FridgeActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadHomeSummary()   // 여기 하나로 통합
    }

    // 홈 요약 데이터 로드
    private fun loadHomeSummary() {
        Thread {
            try {
                val foods = Api.getIngredients()

                val today = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                // 임박 재료
                val soonFoods = foods.filter { food ->
                    val expireDate = LocalDate.parse(food.expiryDate, formatter)
                    val remainDays = ChronoUnit.DAYS.between(today, expireDate)
                    remainDays in 0..SOON_DAYS
                }

                val totalCount = foods.size

                runOnUiThread {
                    updateSoonExpireCard(soonFoods)
                    updateFoodCount(totalCount)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    // 임박 재료 카드
    private fun updateSoonExpireCard(soonFoods: List<FoodItem>) {
        val tvSoonExpire = findViewById<TextView>(R.id.tvSoonExpireItems)

        if (soonFoods.isEmpty()) {
            tvSoonExpire.text = "임박한 재료 없음"
            return
        }

        val names = soonFoods.map { it.name }

        tvSoonExpire.text =
            if (names.size <= 3) {
                names.joinToString(" · ")
            } else {
                names.take(3).joinToString(" · ") + " 등"
            }
    }

    // 총 개수
    private fun updateFoodCount(count: Int) {
        Log.d("HOME_COUNT", "updateFoodCount 호출됨: $count")

        val tv = findViewById<TextView>(R.id.tvFoodCount)
        tv.text = "${count}개"
    }
}
