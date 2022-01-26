package io.appwrite.almostnetflix.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.appwrite.almostnetflix.databinding.ItemFeedCategoryBinding
import io.appwrite.almostnetflix.model.Category
import io.appwrite.almostnetflix.model.Movie

class FeedCategoryAdapter(private val onNestedItemSelected: (Movie) -> Unit) :
    ListAdapter<Map.Entry<Category, Set<Movie>>, FeedCategoryViewHolder>(object :
        DiffUtil.ItemCallback<Map.Entry<Category, Set<Movie>>>() {

        override fun areItemsTheSame(
            oldItem: Map.Entry<Category, Set<Movie>>,
            newItem: Map.Entry<Category, Set<Movie>>,
        ): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(
            oldItem: Map.Entry<Category, Set<Movie>>,
            newItem: Map.Entry<Category, Set<Movie>>,
        ): Boolean {
            return oldItem.value == newItem.value
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedCategoryViewHolder {
        return FeedCategoryViewHolder(
            ItemFeedCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FeedCategoryViewHolder, position: Int) {
        holder.bind(getItem(position), onNestedItemSelected)
    }
}