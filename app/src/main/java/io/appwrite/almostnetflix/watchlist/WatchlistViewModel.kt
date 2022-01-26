package io.appwrite.almostnetflix.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.appwrite.Client
import io.appwrite.almostnetflix.core.BaseViewModel
import io.appwrite.almostnetflix.feed.MovieDataSource
import io.appwrite.almostnetflix.model.Category
import io.appwrite.almostnetflix.model.Movie
import kotlinx.coroutines.launch

class WatchlistViewModel(
    client: Client,
    private val userId: String,
) : BaseViewModel() {

    private val movieDataSource = MovieDataSource(client)

    private val _movies = MutableLiveData<MutableSet<Movie>>()
    private val _selectedMovie = MutableLiveData<Movie?>()

    val movies: LiveData<MutableSet<Movie>> = _movies
    val selectedMovie: LiveData<Movie?> = _selectedMovie

    private val movieSets = mutableMapOf<Category, MutableSet<Movie>>()

    fun fetchWatchlist() {
        viewModelScope.launch {
            val movies = movieDataSource.getMyWatchlist(userId, listOf())
        }
    }

    fun movieSelected(movie: Movie) {
        _selectedMovie.value = movie
    }

    fun resetSelected() {
        _selectedMovie.value = null
    }
}