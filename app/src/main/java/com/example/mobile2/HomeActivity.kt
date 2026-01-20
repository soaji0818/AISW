package com.example.mobile2

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        BottomNavUtil.setup(this, R.id.menu_home)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.selectedItemId = R.id.menu_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> true

                R.id.menu_fridge -> {
                    startActivity(Intent(this, FridgeActivity::class.java))
                    true
                }

                else -> true
            }
        }

        findViewById<TextView>(R.id.tvArrowBalance).setOnClickListener {
            startActivity(Intent(this, FridgeActivity::class.java))
        }

    }
}
