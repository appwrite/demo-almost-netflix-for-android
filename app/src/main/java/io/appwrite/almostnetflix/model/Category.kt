package io.appwrite.almostnetflix.model

data class Category(
    val title: String,
    val queries: List<String>,
    val collectionName: String? = null,
)