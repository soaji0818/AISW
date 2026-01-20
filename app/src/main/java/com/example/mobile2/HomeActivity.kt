package com.example.mobile2

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile2.util.BottomNavUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        BottomNavUtil.setup(this, R.id.menu_home)

        findViewById<TextView>(R.id.tvArrowBalance).setOnClickListener {
            startActivity(Intent(this, FridgeActivity::class.java))
        }
    }
}

