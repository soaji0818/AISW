package com.example.mobile2.data

data class FoodItem(
    val id: Int,
    val title: String,
    val sub: String,
    val price: String,
    val qrText: String,
    var isExpanded: Boolean = false
)