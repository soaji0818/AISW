package com.example.mobile2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.data.AlarmItem
import com.example.mobile2.adapter.AlarmAdapter
import com.example.mobile2.Repository.AlarmRepository
import com.example.mobile2.api.Api
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AlarmActivity : AppCompatActivity() {

    private lateinit var adapter: AlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        findViewById<TextView>(R.id.tvBack).setOnClickListener {
            finish()
        }

        Thread {
            val foods = Api.getIngredients()
            val today = LocalDate.now()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val timeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")

            foods.filter {
                LocalDate.parse(it.expiryDate, formatter).isBefore(LocalDate.now())
            }.forEach { food ->

                val expiredAt = LocalDate
                    .parse(food.expiryDate, formatter)
                    .atStartOfDay()

                AlarmRepository.add(
                    AlarmItem(
                        title = "유통기한 만료",
                        content = "${food.name}의 유통기한이 지났어요.",
                        time = expiredAt.format(timeFormatter)
                    )
                )
            }

            runOnUiThread {
                adapter.submitList(AlarmRepository.getAll())
            }
        }.start()


        adapter = AlarmAdapter()

        val rv = findViewById<RecyclerView>(R.id.rvNotification)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    //최신 알림 반영
    override fun onResume() {
        super.onResume()
        adapter.submitList(AlarmRepository.getAll())
    }
}
