package com.example.mobile2.data

data class FoodItem(
    val id: Int,
    val name: String,
    val category: String,
    val expireDate: String,
    val storageType: String,
    val qrText: String,
    var isExpanded: Boolean = false
)
