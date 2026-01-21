package com.example.mobile2

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class AddFoodActivity : AppCompatActivity() {

    private var selectedDate: LocalDate? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfood)

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

            val tempId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
            val qrText = "FOOD_ID=$tempId"

            val intent = intent.apply {
                putExtra("EXTRA_ID",tempId)
                putExtra("EXTRA_NAME",name)
                putExtra("EXTRA_CATEGORY",category)
                putExtra("EXTRA_EXPIRY",expiry.toString())
                putExtra("EXTRA_STORAGE",storageType)
                putExtra("EXTRA_QR_TEXT",qrText)
            }
            setResult(RESULT_OK, intent)
            finish()

        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}