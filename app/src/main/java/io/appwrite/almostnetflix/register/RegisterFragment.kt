package io.appwrite.almostnetflix.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.appwrite.almostnetflix.R
import io.appwrite.almostnetflix.core.*
import io.appwrite.almostnetflix.databinding.FragmentRegisterBinding
import io.appwrite.almostnetflix.login.AuthViewModel
import io.appwrite.models.User

class RegisterFragment : Fragment() {

    private val viewModel by viewModels<AuthViewModel> {
        ClientViewModelFactory(Configuration.client)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentRegisterBinding.inflate(
            inflater,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val view = binding.root
        val inputs = view.findViewById<Group>(R.id.inputs)
        val pg = view.findViewById<ProgressBar>(R.id.progress)

        viewModel.isBusy.observe(viewLifecycleOwner) { showBusy(it, pg, inputs) }
        viewModel.user.observe(viewLifecycleOwner, ::navigateToFeed)
        viewModel.message.observe(viewLifecycleOwner, ::showMessage)

        return view
    }

    private fun showBusy(enabled: Boolean, progress: View, inputs: Group) = if (enabled) {
        requireActivity().hideSoftKeyBoard()
        progress.visibility = View.VISIBLE
        inputs.visibility = View.INVISIBLE
    } else {
        progress.visibility = View.INVISIBLE
        inputs.visibility = View.VISIBLE
    }

    private fun navigateToFeed(user: User) {
        findNavController().navigate(
            RegisterFragmentDirections.registerToFeedAction(user.id)
        )
    }

    private fun showMessage(message: Message) = when (message.type) {
        Messages.USER_NAME_INVALID -> {
            Snackbar.make(requireView(), R.string.user_name_invalid, Snackbar.LENGTH_LONG).show()
        }
        Messages.USER_PASSWORD_INVALID -> {
            Snackbar.make(requireView(), R.string.user_password_invalid, Snackbar.LENGTH_LONG)
                .show()
        }
        Messages.CREATE_SESSION_FAILED -> {
            Snackbar.make(requireView(), R.string.create_session_failed, Snackbar.LENGTH_LONG)
                .show()
        }
        else -> {
        }
    }
}