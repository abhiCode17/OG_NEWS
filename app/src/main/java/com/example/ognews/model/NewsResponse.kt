package com.app.ognews.model

data class NewsResponse (
    val status: String,
    val totalResult: Int,
    val articles: List<Article>
)