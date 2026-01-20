package com.example.mobile2.data

data class RecipeItem(
    val title: String,
    val reason: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val timeMin: Int
)