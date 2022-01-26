package io.appwrite.almostnetflix.watchlist

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
import io.appwrite.almostnetflix.databinding.FragmentWatchlistBinding
import io.appwrite.almostnetflix.feed.ContentCellAdapter
import io.appwrite.almostnetflix.model.Movie

class WatchlistFragment : Fragment() {

    private val args by navArgs<WatchlistFragmentArgs>()

    private val viewModel by viewModels<WatchlistViewModel> {
        UserViewModelFactory(Configuration.client, args.userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentWatchlistBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = ContentCellAdapter(viewModel::movieSelected)

        binding.watchlistRecycler.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val view = binding.root
        val progressBar = view.findViewById<ProgressBar>(R.id.progress)

        viewModel.movies.observe(viewLifecycleOwner) {
            adapter.submitList(it.toList())
        }
        viewModel.selectedMovie.observe(viewLifecycleOwner) {
            if (it != null) {
                navigateToMovieDetail(it)
                viewModel.resetSelected()
            }
        }
        viewModel.isBusy.observe(viewLifecycleOwner) {
            showBusy(it, progressBar)
        }
        viewModel.message.observe(viewLifecycleOwner, ::showMessage)

        viewModel.fetchWatchlist()

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
            WatchlistFragmentDirections.watchlistToMovieDetailAction(args.userId, movie.id),
        )
    }

    private fun showMessage(message: Message) = when (message.type) {
        else -> {}
    }

}