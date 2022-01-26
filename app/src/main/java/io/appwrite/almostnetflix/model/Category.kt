package io.appwrite.almostnetflix.model

data class Category(
    val title: String,
    val queries: List<String>,
    val orderAttributes: List<String>,
    val orderTypes: List<String>,
    val collectionName: String? = null,
)