package io.appwrite.almostnetflix.feed

import io.appwrite.Client
import io.appwrite.Query
import io.appwrite.almostnetflix.core.Configuration
import io.appwrite.almostnetflix.model.Category
import io.appwrite.almostnetflix.model.Movie
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Database
import io.appwrite.services.Realtime

class MovieDataSource(
    private val client: Client,
) {
    private val database by lazy { Database(client) }
    private val realtime by lazy { Realtime(client) }

    @Throws
    suspend fun getMovies(
        category: Category,
    ): List<Movie> {
        val movieDocuments = database.listDocuments(
            collectionId = category.collectionName ?: Configuration.MOVIE_COLLECTION_ID,
            queries = category.queries,
            limit = null,
            offset = null,
            cursor = null,
            cursorDirection = null,
            orderAttributes = category.orderAttributes,
            orderTypes = category.orderTypes
        )

        return movieDocuments.convertTo(::Movie)
    }

    @Throws
    suspend fun getMovie(movieId: String): Movie {
        val movie = database.getDocument(
            collectionId = Configuration.MOVIE_COLLECTION_ID,
            documentId = movieId
        )
        return Movie(movie.data)
    }

    suspend fun getFeaturedMovie(): Movie? {
        val featured = database.listDocuments(
            collectionId = Configuration.MOVIE_COLLECTION_ID,
            limit = 1,
            orderAttributes = listOf("trendingIndex"),
            orderTypes = listOf("DESC")
        )

        return featured
            .convertTo(::Movie)
            .firstOrNull()
    }

    suspend fun getMyWatchlist(userId: String, movieIds: List<String>): List<Movie> {
        val movies = database.listDocuments(
            collectionId = Configuration.MOVIE_COLLECTION_ID,
            queries = listOf(
                Query.equal("\$id", movieIds)
            )
        )
        return movies.convertTo(::Movie)
    }

    suspend fun addToWatchlist(userId: String, movieId: String): Boolean {
        try {
            database.createDocument(
                collectionId = Configuration.WATCHLIST_COLLECTION_ID,
                documentId = "unique()",
                data = mapOf(
                    "userId" to userId,
                    "movieId" to movieId,
                    "createdAt" to System.currentTimeMillis()
                ),
                read = listOf("user:$userId"),
                write = listOf("user:$userId")
            )
            return true
        } catch (ex: AppwriteException) {
            ex.printStackTrace()
            return false
        }
    }

    suspend fun removeFromWatchlist(userId: String, movieId: String): Boolean {
        val list = try {
            database.listDocuments(
                collectionId = Configuration.WATCHLIST_COLLECTION_ID,
                queries = listOf(
                    Query.equal("userId", value = userId),
                    Query.equal("movieId", value = movieId)
                ),
                limit = 1
            )
        } catch (ex: AppwriteException) {
            ex.printStackTrace()
            return false
        }

        try {
            database.deleteDocument(
                collectionId = Configuration.WATCHLIST_COLLECTION_ID,
                documentId = list.documents.first().id
            )
        } catch (ex: AppwriteException) {
            ex.printStackTrace()
            return false
        }

        return true
    }

    suspend fun getWatchlistedIds(userId: String, movieIds: List<String>): List<String> {
        return try {
            database.listDocuments(
                collectionId = Configuration.WATCHLIST_COLLECTION_ID,
                queries = listOf(
                    Query.equal("userId", value = userId),
                    Query.equal("movieId", value = movieIds)
                )
            ).documents.map { it.id }
        } catch (ex: AppwriteException) {
            ex.printStackTrace()
            listOf()
        }
    }

    fun subscribeToMovies(action: (movie: Movie) -> Unit) {
        realtime.subscribe(
            "collections.${Configuration.MOVIE_COLLECTION_ID}.documents",
            payloadType = Movie::class.java
        ) {
            action(it.payload)
        }
    }

    suspend fun getSimilarMovies(movie: Movie): List<Movie> {
        return try {
            val similar = database.listDocuments(
                collectionId = Configuration.MOVIE_COLLECTION_ID,
                queries = listOf(
                    Query.equal("genres", value = movie.genres)
                )
            )

            return similar.convertTo(::Movie)
        } catch (ex: AppwriteException) {
            ex.printStackTrace()
            listOf()
        }
    }
}