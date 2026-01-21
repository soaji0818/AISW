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
import com.example.mobile2.data.Ingredient
import com.example.mobile2.util.BottomNavUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.button.MaterialButton

class FridgeActivity : AppCompatActivity() {

    private val adapter = FoodAdapter()
    private var allIngredients: List<Ingredient> = emptyList()
    private var selectedCategory: String? = "전체"
    private var searchEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        BottomNavUtil.setup(this, R.id.menu_fridge)

        val recyclerView = findViewById<RecyclerView>(R.id.rvFood)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = null

        searchEditText = findViewById(R.id.etSearch)
        setupSearch()

        findViewById<FloatingActionButton>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, AddFoodActivity::class.java))
        }

        setupCategoryButton()
        loadIngredients()
    }

    override fun onResume() {
        super.onResume()
        loadIngredients()
    }

    private fun loadIngredients() {
        Thread {
            try {
                val list = Api.getIngredients()
                runOnUiThread {
                    allIngredients = list
                    applyFilters()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    toast("목록 불러오기 실패")
                }
            }
        }.start()
    }

    private fun setupCategoryButton() {
        val btnCategory = findViewById<MaterialButton>(R.id.btnCategory)
        val categories = resources.getStringArray(R.array.ingredient_categories)

        btnCategory.text = selectedCategory ?: "전체"

        btnCategory.setOnClickListener {
            AlertDialog.Builder(this)
                .setItems(categories) { _, which ->
                    btnCategory.text = categories[which]
                    selectedCategory = categories[which]
                    applyFilters()
                }
                .show()
        }
    }

    private fun setupSearch() {
        searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                applyFilters()
            }

            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {}
        })
    }

    private fun applyFilters() {
        val query = searchEditText?.text?.toString().orEmpty().trim()
        val category = selectedCategory?.trim().orEmpty()
        val useCategory = category.isNotBlank() && category != "전체" && category != "카테고리"

        val filtered = allIngredients.filter { item ->
            val matchesQuery = query.isBlank() || item.name.contains(query, ignoreCase = true)
            val matchesCategory = !useCategory || item.category == category
            matchesQuery && matchesCategory
        }

        adapter.submitList(filtered)
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

