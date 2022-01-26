package io.appwrite.almostnetflix.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import io.appwrite.almostnetflix.R
import io.appwrite.almostnetflix.core.ContentDetailViewModelFactory
import io.appwrite.almostnetflix.core.Configuration
import io.appwrite.almostnetflix.core.Message
import io.appwrite.almostnetflix.core.hideSoftKeyBoard
import io.appwrite.almostnetflix.databinding.FragmentMovieDetailBinding
import io.appwrite.almostnetflix.feed.ContentCellAdapter
import io.appwrite.almostnetflix.feed.FeedCategoryAdapter

class MovieDetailFragment : Fragment() {

    private val args by navArgs<MovieDetailFragmentArgs>()

    private val viewModel by viewModels<MovieDetailViewModel> {
        ContentDetailViewModelFactory(
            Configuration.client,
            args.userId,
            args.movieId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMovieDetailBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = ContentCellAdapter(viewModel::movieSelected)

        binding.similarMoviesRecycler.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val view = binding.root
        val pg = view.findViewById<ProgressBar>(R.id.progress)

        viewModel.isBusy.observe(viewLifecycleOwner) { showBusy(it, pg) }
        viewModel.message.observe(viewLifecycleOwner, ::showMessage)
        viewModel.similarMovies.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.inWatchlist.observe(viewLifecycleOwner) {
            binding.myListButton.isChecked = it
        }

        return view
    }

    private fun showBusy(enabled: Boolean, progress: View) = if (enabled) {
        requireActivity().hideSoftKeyBoard()
        progress.visibility = View.VISIBLE
    } else {
        progress.visibility = View.INVISIBLE
    }

    private fun showMessage(message: Message) = when (message.type) {
        else -> {}
    }
}