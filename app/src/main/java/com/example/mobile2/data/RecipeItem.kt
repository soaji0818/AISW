package com.example.mobile2.data

data class RecipeItem(
    val id: Long,
    val title: String,
    val image: String? = null,
    val usedIngredients: List<String> = emptyList(),
    val missedIngredients: List<String> = emptyList(),
    val sourceUrl: String?
)