package io.appwrite.almostnetflix.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.appwrite.Client
import io.appwrite.almostnetflix.movie.MovieDetailViewModel

class ContentDetailViewModelFactory(
    private val client: Client,
    private val userId: String,
    private val movieId: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) = when (modelClass) {
        MovieDetailViewModel::class.java -> MovieDetailViewModel(client, userId, movieId) as T
        else -> throw UnsupportedOperationException()
    }
}