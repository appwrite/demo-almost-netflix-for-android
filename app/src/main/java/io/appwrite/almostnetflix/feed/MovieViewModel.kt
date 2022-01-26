package io.appwrite.almostnetflix.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.appwrite.Client
import io.appwrite.almostnetflix.core.BaseViewModel
import io.appwrite.almostnetflix.core.Configuration
import io.appwrite.almostnetflix.model.Category
import io.appwrite.almostnetflix.model.Movie
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieViewModel(
    client: Client,
    val userId: String,
) : BaseViewModel() {

    private val movieDataSource = MovieDataSource(client)

    private val _moviesByCategory = MutableLiveData<MutableMap<Category, MutableSet<Movie>>>()
    private val _selectedMovie = MutableLiveData<Movie?>()
    private val _featuredMovie = MutableLiveData<Movie>()
    private val _featuredInWatchlist = MutableLiveData<Boolean>()

    val moviesByCategory: LiveData<MutableMap<Category, MutableSet<Movie>>> = _moviesByCategory
    val selectedMovie: LiveData<Movie?> = _selectedMovie
    val featuredMovie: LiveData<Movie> = _featuredMovie
    val featuredInWatchlist: LiveData<Boolean> = _featuredInWatchlist

    private val movieSets = mutableMapOf<Category, MutableSet<Movie>>()
    private var watchlist = mutableListOf<Movie>()

    fun fetchFeaturedMovie() {
        viewModelScope.launch {
            val featured = movieDataSource.getFeaturedMovie() ?: return@launch

            watchlist = movieDataSource
                .getMyWatchlist(userId, watchlist.map { it.id })
                .toMutableList()

            withContext(Main) {
                _featuredMovie.value = featured
                _featuredInWatchlist.value = watchlist.any { it.id == featured.id }
            }
        }
    }

    fun fetchMovies() {
        viewModelScope.launch {
            for (category in Configuration.categories) {
                viewModelScope.launch {
                    val movies = movieDataSource.getMovies(category)

                    if (!movieSets.containsKey(category)) {
                        movieSets[category] = movies.toMutableSet()
                    }

                    movieSets[category]?.addAll(movies)

                    withContext(Main) {
                        _moviesByCategory.value = movieSets
                    }
                }
            }
        }
    }

    fun toggleFeaturedInWatchlist() {
        viewModelScope.launch {
            val contained = watchlist.filter { it.id == featuredMovie.value!!.id }
            if (contained.any()) {
                watchlist.removeAll(contained)
                movieDataSource.removeFromWatchlist(userId, featuredMovie.value!!.id)
            } else {
                watchlist.add(featuredMovie.value!!)
                movieDataSource.addToWatchlist(userId, featuredMovie.value!!.id)
            }

            watchlist = movieDataSource
                .getMyWatchlist(userId, watchlist.map { it.id })
                .toMutableList()

            _featuredInWatchlist.value = watchlist.any { it.id == featuredMovie.value!!.id }
        }
    }

    fun featuredMovieSelected() {
        _selectedMovie.value = featuredMovie.value
    }

    fun movieSelected(movie: Movie) {
        _selectedMovie.value = movie
    }

    fun resetSelected() {
        _selectedMovie.value = null
    }
}