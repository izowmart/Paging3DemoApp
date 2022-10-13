package com.example.paging3demoapp.data.remote

import com.example.paging3demoapp.BuildConfig
import com.example.paging3demoapp.model.SearchResult
import com.example.paging3demoapp.model.UnsplashedImage
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {

    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/photos")
    suspend fun getAllImages(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): List<UnsplashedImage>

    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/search/photos")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("per_page") per_page: Int
    ): SearchResult
}