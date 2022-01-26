package io.appwrite.almostnetflix.title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.appwrite.almostnetflix.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentTitleBinding.inflate(
            inflater,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        val view = binding.root

        binding.loginButton.setOnClickListener {
            navigateToLogin()
        }
        binding.registerButton.setOnClickListener {
            navigateToRegister()
        }

        return view
    }

    private fun navigateToLogin() {
        findNavController().navigate(
            TitleFragmentDirections.titleToLoginAction()
        )
    }

    private fun navigateToRegister() {
        findNavController().navigate(
            TitleFragmentDirections.titleToRegisterAction()
        )
    }
}