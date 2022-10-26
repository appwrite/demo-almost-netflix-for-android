package io.appwrite.almostnetflix.feed

import io.appwrite.Client
import io.appwrite.ID.Companion.unique
import io.appwrite.ID.Companion
import io.appwrite.Permission
import io.appwrite.Query
import io.appwrite.Role
import io.appwrite.almostnetflix.core.Configuration
import io.appwrite.almostnetflix.model.Category
import io.appwrite.almostnetflix.model.Movie
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Databases
import io.appwrite.services.Realtime

class MovieDataSource(
    private val client: Client,
) {
    private val databases by lazy { Databases(client) }
    private val realtime by lazy { Realtime(client) }

    @Throws
    suspend fun getMovies(
        category: Category,
    ): List<Movie> {

        val movieDocuments = databases.listDocuments(
            databaseId = Configuration.DATABASE_ID,
            collectionId = category.collectionName ?: Configuration.MOVIE_COLLECTION_ID,
            queries = category.queries
        )

        return movieDocuments.convertTo(::Movie)
    }

    @Throws
    suspend fun getMovie(movieId: String): Movie {
        val movie = databases.getDocument(
            databaseId = Configuration.DATABASE_ID,
            collectionId = Configuration.MOVIE_COLLECTION_ID,
            documentId = movieId
        )
        return Movie(movie.data)
    }

    suspend fun getFeaturedMovie(): Movie? {
        val featured = databases.listDocuments(
            databaseId = Configuration.DATABASE_ID,
            collectionId = Configuration.MOVIE_COLLECTION_ID,
            queries = listOf(
                Query.limit(1),
                Query.orderDesc("trendingIndex")
            ),
        )

        return featured
            .convertTo(::Movie)
            .firstOrNull()
    }

    suspend fun getMyWatchlist(userId: String, movieIds: List<String>): List<Movie> {
        val movies = databases.listDocuments(
            databaseId = Configuration.DATABASE_ID,
            collectionId = Configuration.MOVIE_COLLECTION_ID,
            queries = listOf(
                Query.equal("\$id", value = movieIds),
            )
        )
        return movies.convertTo(::Movie)
    }

    suspend fun addToWatchlist(userId: String, movieId: String): Boolean {
        try {
            databases.createDocument(
                databaseId = Configuration.DATABASE_ID,
                collectionId = Configuration.WATCHLIST_COLLECTION_ID,
                documentId = unique(),
                data = mapOf(
                    "userId" to userId,
                    "movieId" to movieId
                ),
                permissions = listOf(
                    Permission.read(Role.user(userId)),
                    Permission.write(Role.user(userId))
                )
            )
            return true
        } catch (ex: AppwriteException) {
            ex.printStackTrace()
            return false
        }
    }

    suspend fun removeFromWatchlist(userId: String, movieId: String): Boolean {
        val list = try {
            databases.listDocuments(
                databaseId = Configuration.DATABASE_ID,
                collectionId = Configuration.WATCHLIST_COLLECTION_ID,
                queries = listOf(
                    Query.equal("userId", value = userId),
                    Query.equal("movieId", value = movieId),
                    Query.limit(1)
                ),
            )
        } catch (ex: AppwriteException) {
            ex.printStackTrace()
            return false
        }

        try {
            databases.deleteDocument(
                databaseId = Configuration.DATABASE_ID,
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
            databases.listDocuments(
                databaseId = Configuration.DATABASE_ID,
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
            val similar = databases.listDocuments(
                databaseId = Configuration.DATABASE_ID,
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