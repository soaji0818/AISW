package com.example.mobile2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.YearMonth

class MonthlyReportActivity : AppCompatActivity() {

    private lateinit var tvYearMonth: TextView
    private lateinit var tvMonthlyCount: TextView
    private lateinit var tvTopCategory: TextView

    private var currentYearMonth: YearMonth = YearMonth.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_report)

        // 뒤로가기
        findViewById<TextView>(R.id.tvBack).setOnClickListener {
            finish()
        }

        tvYearMonth = findViewById(R.id.tvYearMonth)
        tvMonthlyCount = findViewById(R.id.tvMonthlyCount)
        tvTopCategory = findViewById(R.id.tvTopCategory)

        val btnPrevMonth = findViewById<TextView>(R.id.btnPrevMonth)
        val btnNextMonth = findViewById<TextView>(R.id.btnNextMonth)

        // 초기 데이터 표시
        updateMonthText()
        loadDummyReportData()

        // 이전 달
        btnPrevMonth.setOnClickListener {
            currentYearMonth = currentYearMonth.minusMonths(1)
            updateMonthText()
            loadDummyReportData()
        }

        //  다음 달
        btnNextMonth.setOnClickListener {
            currentYearMonth = currentYearMonth.plusMonths(1)
            updateMonthText()
            loadDummyReportData()
        }

        //  컬리 바로가기
        findViewById<TextView>(R.id.btnGoKurly).setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.kurly.com/main")
            )
            startActivity(intent)
        }
    }

    /**
     * 상단 연/월 텍스트 갱신
     */
    private fun updateMonthText() {
        tvYearMonth.text = "${currentYearMonth.year}년 ${currentYearMonth.monthValue}월"
    }

    /**
     *  더미 리포트 데이터
     * (나중에 DB/Firebase로 교체)
     */
    private fun loadDummyReportData() {
        // 더미 값
        val totalCount = 12
        val topCategory = "과일"

        tvMonthlyCount.text = "등록한 식재료: ${totalCount}개"
        tvTopCategory.text = "가장 많이 등록한 카테고리: $topCategory"

        //  차트 데이터는 여기서 세팅 예정
        // setupPieChart(...)
    }
}
