package com.example.mobile2

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.example.mobile2.data.FoodItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.util.BottomNavUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.button.MaterialButton

class FridgeActivity : AppCompatActivity() {
    private val items = mutableListOf<FoodItem>()
    private lateinit var adapter: FoodAdapter
    private var tempId = 1


    private val addFoodLauncher =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data ?: return@registerForActivityResult

                val name = data.getStringExtra("EXTRA_NAME") ?: return@registerForActivityResult
                val category = data.getStringExtra("EXTRA_CATEGORY") ?: ""
                val expiry = data.getStringExtra("EXTRA_EXPIRY") ?: ""
                val storage = data.getStringExtra("EXTRA_STORAGE") ?: ""

                val idFromAdd = data.getIntExtra("EXTRA_ID", -1)
                val id = if(idFromAdd != -1) idFromAdd else tempId++

                val qrText = "FOOD_ID=$id"
                items.add(
                    FoodItem(
                        id = id,
                        title = name,
                        sub = "$category · $storage",
                        price = "유통기한: $expiry",
                        qrText = qrText
                    )
                )
                adapter.notifyItemInserted(items.lastIndex)
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        BottomNavUtil.setup(this, R.id.menu_fridge)

        val recyclerView = findViewById<RecyclerView>(R.id.rvFood)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = null

        adapter = FoodAdapter(items)
        recyclerView.adapter = adapter


        findViewById<FloatingActionButton>(R.id.btnAdd).setOnClickListener {
            addFoodLauncher.launch(Intent(this, AddFoodActivity::class.java))
        }

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

