package com.app.ognews.retrofitapi

import com.app.ognews.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything")
    fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = "c74b69247e684ce39db3c81c0624064e"
    ): Call<NewsResponse>

//    @GET("top-headlines")
//    fun fetchNewsByCategory(
//        @Query("q") query: String,
//        @Query("apiKey") apiKey: String = "c74b69247e684ce39db3c81c0624064e"
//    ): Call<NewsResponse>

}