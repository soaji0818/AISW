package com.example.mobile2.data

data class FoodItem(
    val title: String,
    val sub: String,
    val price: String,
    var isExpanded: Boolean = false
)