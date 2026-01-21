package com.example.mobile2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.adapter.FoodAdapter
import com.example.mobile2.data.FoodItem
import com.example.mobile2.util.BottomNavUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        // 하단 네비
        BottomNavUtil.setup(this, R.id.menu_fridge)

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvFood)
        recyclerView.itemAnimator = null
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ 더미 데이터
        val dummyList = mutableListOf(
            FoodItem(
                name = "사과",
                category = "과일",
                expireDate = "2026-11-12",
                isExpanded = false
            )
        )

        recyclerView.adapter = FoodAdapter(dummyList)

        // 상품 추가 버튼
        findViewById<FloatingActionButton>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, AddFoodActivity::class.java))
        }

        // 카테고리 버튼
        setupCategoryButton()
    }

    private fun setupCategoryButton() {
        val btnCategory = findViewById<MaterialButton>(R.id.btnCategory)
        val categories = resources.getStringArray(R.array.ingredient_categories)

        btnCategory.setOnClickListener {
            AlertDialog.Builder(this)
                .setItems(categories) { _, which ->
                    btnCategory.text = categories[which]
                }
                .show()
        }
    }
}
