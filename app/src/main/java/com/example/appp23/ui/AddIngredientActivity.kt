package com.example.appp23.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appp23.R
import java.time.LocalDate

class AddIngredientActivity : AppCompatActivity() {

    private var selectedDate: LocalDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        val etName = findViewById<EditText>(R.id.etName)
        val spCategory = findViewById<Spinner>(R.id.spCategory)
        val tvExpiry = findViewById<TextView>(R.id.tvExpiry)
        val btnPickDate = findViewById<Button>(R.id.btnPickDate)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val categories = resources.getStringArray(R.array.ingredient_categories)
        spCategory.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories.toList())

        btnPickDate.setOnClickListener {
            val now = LocalDate.now()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    selectedDate = LocalDate.of(y, m + 1, d)
                    tvExpiry.text = selectedDate.toString()
                },
                now.year,
                now.monthValue - 1,
                now.dayOfMonth
            ).show()
        }

        btnSave.setOnClickListener {
            val name = etName.text?.toString().orEmpty().trim()
            val category = spCategory.selectedItem?.toString().orEmpty()
            val expiry = selectedDate
            val storageType =
                if (findViewById<RadioButton>(R.id.rbFreezer).isChecked) "FREEZER" else "FRIDGE"

            if (name.isBlank()) {
                toast("상품 이름을 입력해줘")
                return@setOnClickListener
            }
            if (category == "선택") {
                toast("카테고리를 선택해줘")
                return@setOnClickListener
            }
            if (expiry == null) {
                toast("유통기한을 선택해줘")
                return@setOnClickListener
            }

            toast("입력 완료: $name / $category / ${expiry} / $storageType")
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
