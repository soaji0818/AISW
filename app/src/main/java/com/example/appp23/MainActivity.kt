package com.example.appp23

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appp23.ui.AddIngredientActivity
import com.example.appp23.ui.IngredientAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val rv = findViewById<RecyclerView>(R.id.rvIngredients)
        adapter = IngredientAdapter()
        rv.adapter = adapter
        adapter.submitList(emptyList())

        findViewById<Button>(R.id.btnGoAdd).setOnClickListener {
            startActivity(Intent(this, AddIngredientActivity::class.java))
        }
    }
}
