package com.example.mobile2.data

data class FoodItem(
    val id: Int,
    val name: String,
    val category: String,
    val expireDate: String,
    var isExpanded: Boolean = false
)
