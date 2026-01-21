package com.example.mobile2.api

import com.example.mobile2.data.RecipeItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes/recommendations")
    suspend fun recommendRecipes(
        @Query("ingredients") ingredients: String,
        @Query("limit") limit: Int = 10
    ): Response<List<RecipeItem>>
}
