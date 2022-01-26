package io.appwrite.almostnetflix.feed

import io.appwrite.almostnetflix.core.BaseViewModel
import io.appwrite.almostnetflix.model.Movie

class FeedCategoryViewModel(
    val categoryName: String,
    val movies: List<Movie>,
) : BaseViewModel()