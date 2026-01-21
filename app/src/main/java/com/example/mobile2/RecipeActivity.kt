package com.example.mobile2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.adapter.RecipeAdapter
import com.example.mobile2.api.RecipeApiClient
import com.example.mobile2.util.BottomNavUtil
import kotlinx.coroutines.launch

class RecipeActivity : AppCompatActivity() {

    private lateinit var adapter: RecipeAdapter
    private val recipeApi = RecipeApiClient.api
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        // 하단 네비
        BottomNavUtil.setup(this, R.id.menu_recipe)

        val etOwned = findViewById<EditText>(R.id.etOwned)
        val etQuery = findViewById<EditText>(R.id.etQuery)
        val spSort = findViewById<Spinner>(R.id.spSort)
        val btn = findViewById<Button>(R.id.btnRecommend)
        tvStatus = findViewById(R.id.tvStatus)
        val rv = findViewById<RecyclerView>(R.id.rvRecipes)

        adapter = RecipeAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        spSort.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.sort_options).toList()
        )

        // 버튼 누르면 서버 추천 호출
        btn.setOnClickListener {
            val raw = etOwned.text.toString().trim()
            val ingredients = raw
                .split(Regex("[,\\s]+"))
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .joinToString(",")
            if (ingredients.isBlank()) {
                toast("재료를 입력해줘")
                return@setOnClickListener
            }

            tvStatus.text = "추천 불러오는 중..."
            loadRecipes(ingredients)
        }
    }

    private fun loadRecipes(ingredients: String) {
        lifecycleScope.launch {
            try {
                val res = recipeApi.recommendRecipes(ingredients, limit = 10)
                if (res.isSuccessful) {
                    val list = res.body().orEmpty()
                    adapter.submitList(list)
                    tvStatus.text = "추천 결과 (${list.size})"
                } else {
                    toast("추천 실패: ${res.code()}")
                    tvStatus.text = "추천 실패"
                }
            } catch (e: Exception) {
                toast("네트워크 오류")
                tvStatus.text = "추천 실패"
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}