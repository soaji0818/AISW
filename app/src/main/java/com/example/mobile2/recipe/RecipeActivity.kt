package com.example.mobile2.recipe

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.R
import com.example.mobile2.data.RecipeItem
import com.example.mobile2.util.BottomNavUtil

class RecipeActivity : AppCompatActivity() {

    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        // 하단 네비
        BottomNavUtil.setup(this, R.id.menu_recipe)

        val etOwned = findViewById<EditText>(R.id.etOwned)
        val etQuery = findViewById<EditText>(R.id.etQuery)
        val spSort = findViewById<Spinner>(R.id.spSort)
        val btn = findViewById<Button>(R.id.btnRecommend)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val rv = findViewById<RecyclerView>(R.id.rvRecipes)

        adapter = RecipeAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        spSort.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.sort_options).toList()
        )

        // ✅ 버튼 누르면 더미 데이터 표시
        btn.setOnClickListener {
            tvStatus.text = "추천 결과"

            adapter.submitList(
                listOf(
                    RecipeItem(
                        title = "김치볶음밥",
                        reason = "집에 있는 재료로 가능",
                        ingredients = listOf("김치", "밥", "계란"),
                        steps = listOf("김치 볶기", "밥 넣기", "계란 추가"),
                        timeMin = 15
                    ),
                    RecipeItem(
                        title = "계란말이",
                        reason = "간단하고 빠름",
                        ingredients = listOf("계란", "소금"),
                        steps = listOf("계란 풀기", "말아서 굽기"),
                        timeMin = 10
                    )
                )
            )
        }
    }
}
