package io.appwrite.almostnetflix.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.appwrite.almostnetflix.R
import io.appwrite.almostnetflix.core.Configuration
import io.appwrite.almostnetflix.core.Message
import io.appwrite.almostnetflix.core.UserViewModelFactory
import io.appwrite.almostnetflix.core.hideSoftKeyBoard
import io.appwrite.almostnetflix.databinding.FragmentFeedBinding
import io.appwrite.almostnetflix.model.Movie

class FeedFragment : Fragment() {

    private val args by navArgs<FeedFragmentArgs>()

    private val viewModel by viewModels<MovieViewModel> {
        UserViewModelFactory(Configuration.client, args.userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = FeedCategoryAdapter(viewModel::movieSelected)

        binding.categoryRecycler.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val view = binding.root
        val progressBar = view.findViewById<ProgressBar>(R.id.progress)

        viewModel.moviesByCategory.observe(viewLifecycleOwner) {
            adapter.submitList(it.entries.toList())
        }
        viewModel.selectedMovie.observe(viewLifecycleOwner) {
            if (it != null) {
                navigateToMovieDetail(it)
                viewModel.resetSelected()
            }
        }
        viewModel.featuredInWatchlist.observe(viewLifecycleOwner) {
            binding.myListButton.isChecked = it
        }
        viewModel.isBusy.observe(viewLifecycleOwner) {
            showBusy(it, progressBar)
        }
        viewModel.message.observe(viewLifecycleOwner, ::showMessage)

        viewModel.fetchMovies()
        viewModel.fetchFeaturedMovie()

        return view
    }

    private fun showBusy(enabled: Boolean, progress: View) = if (enabled) {
        requireActivity().hideSoftKeyBoard()
        progress.visibility = View.VISIBLE
    } else {
        progress.visibility = View.INVISIBLE
    }

    private fun navigateToMovieDetail(movie: Movie) {
        findNavController().navigate(
            FeedFragmentDirections.feedToMovieDetailAction(args.userId, movie.id),
        )
    }

    private fun showMessage(message: Message) = when (message.type) {
        else -> {}
    }
}