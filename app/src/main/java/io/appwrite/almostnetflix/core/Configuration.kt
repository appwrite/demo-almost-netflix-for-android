package io.appwrite.almostnetflix.core

import io.appwrite.Client
import io.appwrite.Query
import io.appwrite.almostnetflix.model.Category

object Configuration {

    lateinit var client: Client

    const val ENDPOINT = "https://demo.appwrite.io/v1"
    const val PROJECT_ID = "almostNetflix2"
    const val DATABASE_ID = "YOUR_DATABASE_ID"
    const val MOVIE_COLLECTION_ID = "YOUR_COLLECTION_ID"
    const val WATCHLIST_COLLECTION_ID = "YOUR_COLLECTION_ID"
    const val STORAGE_BUCKET_ID = "YOUR_BUCKET_ID"

    val categories = listOf(
        Category(
            title = "Popular this week",
            queries = listOf(),
            orderAttributes = listOf("trendingIndex"),
            orderTypes = listOf("DESC")
        ),
        Category(
            title = "Only on Almost Netlfix",
            queries = listOf(Query.equal("isOriginal", true)),
            orderAttributes = listOf("trendingIndex"),
            orderTypes = listOf("DESC")
        ),
        Category(
            title = "New Releases",
            queries = listOf(Query.greaterThanEqual("releaseDate", 2018)),
            orderAttributes = listOf("releaseDate"),
            orderTypes = listOf("DESC")
        ),
        Category(
            title = "Movies longer than 2 hours",
            queries = listOf(Query.greaterThanEqual("durationMinutes", 120)),
            orderAttributes = listOf("durationMinutes"),
            orderTypes = listOf("DESC")
        ),
        Category(
            title = "Love is in the air",
            queries = listOf(Query.search("genres", "romance")),
            orderAttributes = listOf("trendingIndex"),
            orderTypes = listOf("DESC")
        ),
        Category(
            title = "Animated worlds",
            queries = listOf(Query.search("genres", "Animation")),
            orderAttributes = listOf("trendingIndex"),
            orderTypes = listOf("DESC")
        ),
        Category(
            title = "It is getting scary",
            queries = listOf(Query.search("genres", "Horror")),
            orderAttributes = listOf("trendingIndex"),
            orderTypes = listOf("DESC")
        ),
        Category(
            title = "Sci-Fi awaits...",
            queries = listOf(Query.search("genres", "Science Fiction")),
            orderAttributes = listOf("trendingIndex"),
            orderTypes = listOf("DESC")
        ),
        Category(
            title = "Anime",
            queries = listOf(Query.search("tags", "Anime")),
            orderAttributes = listOf("trendingIndex"),
            orderTypes = listOf("DESC")
        ),
    )
}