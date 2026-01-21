package com.example.mobile2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.adapter.FoodAdapter
import com.example.mobile2.api.Api
import com.example.mobile2.data.FoodItem
import com.example.mobile2.util.BottomNavUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FridgeActivity : AppCompatActivity() {

    private val adapter = FoodAdapter()
    private var allFoodList: List<FoodItem> = emptyList()
    private var selectedCategory: String = "전체"
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        // 하단 네비
        BottomNavUtil.setup(this, R.id.menu_fridge)

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvFood)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = null

        // 검색
        searchEditText = findViewById(R.id.etSearch)
        setupSearch()

        // 추가 버튼
        findViewById<FloatingActionButton>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, AddFoodActivity::class.java))
        }

        // 카테고리 필터
        setupCategoryButton()
    }

    override fun onResume() {
        super.onResume()
        loadFoods()
    }

    /* ================== 서버에서 목록 불러오기 ================== */
    private fun loadFoods() {
        Thread {
            try {
                val list = Api.getIngredients()
                runOnUiThread {
                    allFoodList = list
                    applyFilters()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    toast("목록 불러오기 실패")
                }
            }
        }.start()
    }

    /* ================== 검색 ================== */
    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                applyFilters()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    /* ================== 카테고리 필터 ================== */
    private fun setupCategoryButton() {
        val btnCategory = findViewById<MaterialButton>(R.id.btnCategory)
        val categories = resources.getStringArray(R.array.ingredient_categories)

        btnCategory.text = selectedCategory

        btnCategory.setOnClickListener {
            AlertDialog.Builder(this)
                .setItems(categories) { _, which ->
                    selectedCategory = categories[which]
                    btnCategory.text = selectedCategory
                    applyFilters()
                }
                .show()
        }
    }

    /* ================== 검색 + 카테고리 적용 ================== */
    private fun applyFilters() {
        val query = searchEditText.text.toString().trim()
        val useCategory =
            selectedCategory.isNotBlank() &&
                    selectedCategory != "전체" &&
                    selectedCategory != "선택"

        val filtered = allFoodList.filter { food ->
            val matchName =
                query.isBlank() || food.name.contains(query, ignoreCase = true)
            val matchCategory =
                !useCategory || food.category == selectedCategory

            matchName && matchCategory
        }

        adapter.submitList(filtered)
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
