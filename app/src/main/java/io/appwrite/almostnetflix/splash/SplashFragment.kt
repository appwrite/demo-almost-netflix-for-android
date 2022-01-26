package io.appwrite.almostnetflix.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.appwrite.almostnetflix.core.*
import io.appwrite.almostnetflix.databinding.FragmentSplashBinding
import io.appwrite.models.User

class SplashFragment : Fragment() {

    private val viewModel by viewModels<SplashViewModel> {
        ClientViewModelFactory(Configuration.client)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentSplashBinding.inflate(
            inflater,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val view = binding.root

        viewModel.authUser.observe(viewLifecycleOwner) {
            if (it != null) {
                navigateToFeed(it)
                return@observe
            }
            navigateToTitle()
        }

        return view
    }

    private fun navigateToTitle() {
        findNavController().navigate(
            SplashFragmentDirections.splashToTitleAction()
        )
    }

    private fun navigateToFeed(user: User) {
        findNavController().navigate(
            SplashFragmentDirections.splashToFeedAction(user.id)
        )
    }
}