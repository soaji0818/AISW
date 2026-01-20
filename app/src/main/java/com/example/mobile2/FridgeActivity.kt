package com.example.mobile2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile2.util.BottomNavUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.button.MaterialButton

class FridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        BottomNavUtil.setup(this, R.id.menu_fridge)

        val recyclerView = findViewById<RecyclerView>(R.id.rvFood)
        recyclerView.itemAnimator = null

        findViewById<FloatingActionButton>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, AddFoodActivity::class.java))
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

