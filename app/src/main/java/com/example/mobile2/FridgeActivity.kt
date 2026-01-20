package com.example.mobile2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        BottomNavUtil.setup(this, R.id.menu_home)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // 현재 메뉴 체크
        bottomNav.menu.findItem(R.id.menu_fridge).isChecked = true

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.menu_fridge -> true
                else -> true
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rvFood)
        recyclerView.itemAnimator = null

        findViewById<FloatingActionButton>(R.id.btnAdd).setOnClickListener {
            val intent = Intent(this, AddFoodActivity::class.java)
            startActivity(intent)
        }
    }
}
