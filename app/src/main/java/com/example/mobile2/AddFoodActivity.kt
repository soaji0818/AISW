package com.example.mobile2

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile2.api.Api
import com.example.mobile2.data.FoodItem
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

        /* ---------- 날짜 선택 ---------- */
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

        /* ---------- 저장 ---------- */
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val category = spCategory.selectedItem.toString()
            val expiry = selectedDate
            val storageType =
                if (findViewById<RadioButton>(R.id.rbFreezer).isChecked) "FREEZER" else "FRIDGE"

            when {
                name.isBlank() -> toast("상품 이름을 입력해줘")
                category == "선택" -> toast("카테고리를 선택해줘")
                expiry == null -> toast("유통기한을 선택해줘")
                else -> saveFood(name, category, expiry, storageType)
            }
        }
    }

    /* ---------- 서버 저장 ---------- */
    private fun saveFood(
        name: String,
        category: String,
        expiry: LocalDate,
        storageType: String
    ) {
        Thread {
            try {
                Api.addIngredient(
                    FoodItem(
                        id = 0, // 서버에서 자동 생성
                        name = name,
                        category = category,
                        expiryDate = expiry.toString(),
                        storageType = storageType,
                        status = "SAFE"
                    )
                )

                runOnUiThread {
                    toast("저장 완료!")
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    toast("저장 실패")
                }
            }
        }.start()
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
