package io.appwrite.almostnetflix.feed

import androidx.recyclerview.widget.RecyclerView
import io.appwrite.almostnetflix.BR
import io.appwrite.almostnetflix.databinding.ItemFeedCategoryBinding
import io.appwrite.almostnetflix.model.Category
import io.appwrite.almostnetflix.model.Movie

class FeedCategoryViewHolder(
    private val binding: ItemFeedCategoryBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Map.Entry<Category, Set<Movie>>, onNestedItemSelected: (Movie) -> Unit) {
        val movieList = item.value.toList()

        val vm = FeedCategoryViewModel(
            categoryName = item.key.title,
            movies = movieList
        )
        val adapter = ContentCellAdapter(onNestedItemSelected)

        binding.setVariable(BR.viewModel, vm)
        binding.categoryRecycler.adapter = adapter

        adapter.submitList(movieList)
    }
}