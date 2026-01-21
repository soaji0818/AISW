package com.example.mobile2.data

data class FoodItem(
    val id: Int,
    val name: String,
    val category: String,
    val expiryDate: String,
    val storageType: String,
    val status : String,
    var isExpanded: Boolean = false
)
