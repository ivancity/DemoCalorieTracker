package com.ivan.m.tracker_data.remote

import com.ivan.m.tracker_data.remote.dto.SearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFoodApi {
    @GET("cgi/search.pl?search_simple=1&json=1&action=process&fields=product_name,nutriments,image_front_thumb_url")
    suspend fun searchFood(
        @Query("search_terms") query: String,
        @Query("page") page: String,
        @Query("page_size") pageSize: String,
    ): SearchDto

    companion object {
        const val BASE_URL = "https://us.openfoodfacts.org/"
    }
}