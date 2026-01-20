package com.example.mobile2.data

data class Ingredient(
    val id: Long? = null,
    val name: String,
    val category: String,
    val expiryDate: String,
    val storageType: String,
    val status: String? = null
)
