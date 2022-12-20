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
            queries = listOf(
                Query.orderDesc("trendingIndex")
            )
        ),
        Category(
            title = "Only on Almost Netflix",
            queries = listOf(
                Query.equal("isOriginal", true),
                Query.orderDesc("trendingIndex")
            ),
        ),
        Category(
            title = "New Releases",
            queries = listOf(
                Query.greaterThanEqual("releaseDate", 2018),
                Query.orderDesc("releaseDate")
            ),
        ),
        Category(
            title = "Movies longer than 2 hours",
            queries = listOf(
                Query.greaterThanEqual("durationMinutes", 120),
                Query.orderDesc("durationMinutes")
            ),
        ),
        Category(
            title = "Love is in the air",
            queries = listOf(
                Query.search("genres", "romance"),
                Query.orderDesc("trendingIndex")
            ),
        ),
        Category(
            title = "Animated worlds",
            queries = listOf(
                Query.search("genres", "Animation"),
                Query.orderDesc("trendingIndex")
            ),
        ),
        Category(
            title = "It is getting scary",
            queries = listOf(
                Query.search("genres", "Horror"),
                Query.orderDesc("trendingIndex")
            ),
        ),
        Category(
            title = "Sci-Fi awaits...",
            queries = listOf(
                Query.search("genres", "Science Fiction"),
                Query.orderDesc("trendingIndex")
            ),

        ),
        Category(
            title = "Anime",
            queries = listOf(
                Query.search("tags", "Anime"),
                Query.orderDesc("trendingIndex")
            ),
        ),
    )
}