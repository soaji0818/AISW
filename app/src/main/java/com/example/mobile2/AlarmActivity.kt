package com.example.mobile2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.data.AlarmItem
import com.example.mobile2.adapter.AlarmAdapter

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

        // 임시 데이터
        val dummyList = listOf(
            AlarmItem(
                title = "이상 냄새 감지",
                content = "이상 냄새가 감지되었어요.\n지금 확인해보세요.",
                time = "오전 10:28"
            ),
            AlarmItem(
                title = "유통기한 알림",
                content = "치즈의 유통기한이 얼마 남지 않았어요.\n지금 확인하고 활용해보세요.",
                time = "오전 9:36"
            )
        )

        rvNotification.adapter = AlarmAdapter(dummyList)
    }
}
