package io.appwrite.almostnetflix.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.appwrite.Client
import io.appwrite.almostnetflix.core.BaseViewModel
import io.appwrite.almostnetflix.feed.MovieDataSource
import io.appwrite.almostnetflix.model.Movie
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MovieDetailViewModel(
    client: Client,
    private val userId: String,
    private val movieId: String
) : BaseViewModel() {

    private val movieDataSource = MovieDataSource(client)

    private val _movie = MutableLiveData<Movie>()
    private val _similarMovies = MutableLiveData<List<Movie>>()
    private val _releaseYear = MutableLiveData<String>()
    private val _duration = MutableLiveData<String>()
    private val _inWatchlist = MutableLiveData<Boolean>()

    val movie: LiveData<Movie> = _movie
    val similarMovies: LiveData<List<Movie>> = _similarMovies
    val releaseYear: LiveData<String> = _releaseYear
    val duration: LiveData<String> = _duration
    val inWatchlist: LiveData<Boolean> = _inWatchlist

    private var watchlist = mutableListOf<Movie>()

    init {
        getMovie()
    }

    fun getWatchlist() {
        viewModelScope.launch {
            watchlist = movieDataSource
                .getMyWatchlist(userId, watchlist.map { it.id })
                .toMutableList()

            _inWatchlist.value = watchlist.any { it.id == movie.value!!.id }
        }
    }

    fun toggleInWatchlist() {
        viewModelScope.launch {
            val contained = watchlist.filter { it.id == movie.value!!.id }
            if (contained.any()) {
                watchlist.removeAll(contained)
                movieDataSource.removeFromWatchlist(userId, movie.value!!.id)
            } else {
                watchlist.add(movie.value!!)
                movieDataSource.addToWatchlist(userId, movie.value!!.id)
            }
            getWatchlist()
        }
    }

    fun movieSelected(movie: Movie) {
        _movie.value = movie
        _similarMovies.value = listOf()
        getDuration()
        getReleaseYear()
        getSimilarMovies()
        getWatchlist()
    }

    private fun getReleaseYear() {
        with(Calendar.getInstance()) {
            timeInMillis = movie.value!!.releaseDate

            _releaseYear.value = get(Calendar.YEAR).toString()
        }
    }

    private fun getDuration() {
        val mins = movie.value!!.durationMinutes.toInt()
        val hours = mins / 60
        val minutes = mins % 60
        _duration.value = "${hours}h ${minutes}m"
    }

    private fun getMovie() {
        viewModelScope.launch {
            val movie = movieDataSource.getMovie(movieId)

            withContext(Main) {
                _movie.value = movie
                getDuration()
                getReleaseYear()
                getSimilarMovies()
            }
        }
    }

    private fun getSimilarMovies() {
        viewModelScope.launch {
            val similar = movieDataSource.getSimilarMovies(_movie.value!!)

            withContext(Main) {
                _similarMovies.value = similar
            }
        }
    }
}