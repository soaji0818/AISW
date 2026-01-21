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

    //  전체 리스트 / 필터된 리스트 / 어댑터
    private lateinit var foodAdapter: FoodAdapter
    private val allFoodList = mutableListOf<FoodItem>()
    private val filteredList = mutableListOf<FoodItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        // 하단 네비
        BottomNavUtil.setup(this, R.id.menu_fridge)

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvFood)
        recyclerView.itemAnimator = null
        recyclerView.layoutManager = LinearLayoutManager(this)

        //  더미 데이터
        allFoodList.addAll(
            listOf(
                FoodItem(
                    id = 1,
                    name = "사과",
                    category = "과일",
                    expireDate = "2026-11-12",
                    storageType = "FRIDGE",
                    qrText = "FOOD_ID=1"
                ),
                FoodItem(
                    id = 2,
                    name = "우유",
                    category = "유제품",
                    expireDate = "2026-02-01",
                    storageType = "FRIDGE",
                    qrText = "FOOD_ID=2"
                ),
                FoodItem(
                    id = 3,
                    name = "상추",
                    category = "채소",
                    expireDate = "2026-01-20",
                    storageType = "FRIDGE",
                    qrText = "FOOD_ID=3"
                ),
                FoodItem(
                    id = 4,
                    name = "주스",
                    category = "음료",
                    expireDate = "2026-03-01",
                    storageType = "FRIDGE",
                    qrText = "FOOD_ID=4"
                )
            )
        )


        // 처음엔 전체 보여주기
        filteredList.addAll(allFoodList)

        foodAdapter = FoodAdapter(filteredList)
        recyclerView.adapter = foodAdapter

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
                    val selectedCategory = categories[which]
                    btnCategory.text = selectedCategory
                    filterByCategory(selectedCategory)
                }
                .show()
        }
    }

    // 카테고리 필터링
    private fun filterByCategory(category: String) {
        filteredList.clear()

        if (category == "선택") {
            filteredList.addAll(allFoodList)
        } else {
            filteredList.addAll(
                allFoodList.filter { it.category == category }
            )
        }

        foodAdapter.notifyDataSetChanged()
    }
}
