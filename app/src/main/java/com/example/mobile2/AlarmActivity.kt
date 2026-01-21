package com.example.mobile2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.data.AlarmItem
import com.example.mobile2.adapter.AlarmAdapter
import com.example.mobile2.Repository.AlarmRepository

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        // 뒤로가기
        findViewById<android.widget.TextView>(R.id.tvBack).setOnClickListener {
            finish()
        }

        // RecyclerView
        val rvNotification = findViewById<RecyclerView>(R.id.rvNotification)
        rvNotification.layoutManager = LinearLayoutManager(this)

        rvNotification.adapter = AlarmAdapter(
            AlarmRepository.getAll()
        )
    }
}
