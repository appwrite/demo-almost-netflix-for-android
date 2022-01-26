package io.appwrite.almostnetflix.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.appwrite.almostnetflix.core.Configuration
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import io.appwrite.services.Account
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _authUser = MutableLiveData<User?>()
    val authUser: LiveData<User?> = _authUser

    init {
        viewModelScope.launch {
            val account = Account(Configuration.client)
            try {
                val user = account.get()
                _authUser.value = user
            } catch (ex: AppwriteException) {
                _authUser.value = null
            }
        }
    }
}