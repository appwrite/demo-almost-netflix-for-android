package io.appwrite.almostnetflix.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.appwrite.Client
import io.appwrite.almostnetflix.feed.MovieViewModel
import io.appwrite.almostnetflix.watchlist.WatchlistViewModel

class UserViewModelFactory(
    private val client: Client,
    private val userId: String,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>) = when (modelClass) {
        MovieViewModel::class.java -> {
            MovieViewModel(client, userId) as T
        }
        WatchlistViewModel::class.java -> {
            WatchlistViewModel(client, userId) as T
        }
        else -> {
            throw UnsupportedOperationException()
        }
    }

}
