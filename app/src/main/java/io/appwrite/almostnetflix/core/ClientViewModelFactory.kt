package io.appwrite.almostnetflix.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.appwrite.Client
import io.appwrite.almostnetflix.login.AuthViewModel
import io.appwrite.almostnetflix.splash.SplashViewModel

class ClientViewModelFactory(
    private val client: Client,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>) = when (modelClass) {
        SplashViewModel::class.java -> {
            SplashViewModel() as T
        }
        AuthViewModel::class.java -> {
            AuthViewModel(client) as T
        }
        else -> {
            throw UnsupportedOperationException()
        }
    }
}