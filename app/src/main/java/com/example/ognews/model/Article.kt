package com.app.ognews.model

data class Article(
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val url: String?,
    val imagePath: String? = null
)
