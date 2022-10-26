package io.appwrite.almostnetflix.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.appwrite.Client
import io.appwrite.ID.Companion.unique
import io.appwrite.almostnetflix.core.*
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.Account as AccountModel
import io.appwrite.services.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val client: Client) : BaseViewModel() {

    val name = MutableStateFlow("")
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _user = MutableLiveData<AccountModel>()

    val user: LiveData<AccountModel> = _user

    private val account by lazy { Account(client) }

    fun getAccount() {
        viewModelScope.launch {
            try {
                val user = account.get()
                _user.value = user
            } catch (ex: Exception) {

            }
        }
    }

    fun login() {
        viewModelScope.launch {
            setBusy(true)

            if (!validateInputs()) {
                setBusy(false)
                return@launch
            }

            try {
                account.createEmailSession(
                    username.value,
                    password.value
                )
                getAccount()
            } catch (ex: AppwriteException) {
                ex.printStackTrace()
                postMessage(
                    Messages.CREATE_SESSION_FAILED,
                    listOf(ex.message ?: ex.response ?: "")
                )
                return@launch
            } finally {
                setBusy(false)
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            setBusy(true)

            if (!validateInputs()) {
                setBusy(false)
                return@launch
            }

            try {
                account.create(
                   userId = unique(),
                   name = name.value,
                   email = username.value,
                   password = password.value
                )
                login()
            } catch (ex: AppwriteException) {
                ex.printStackTrace()
                postMessage(
                    Messages.CREATE_SESSION_FAILED,
                    listOf(ex.message ?: ex.response ?: "")
                )
                return@launch
            } finally {
                setBusy(false)
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (!isValidUserName()) {
            postMessage(Messages.USER_NAME_INVALID)
            return false
        }
        if (!isValidPassword()) {
            postMessage(Messages.USER_PASSWORD_INVALID)
            return false
        }
        return true
    }

    private fun isValidUserName(): Boolean {
        return username.value.length >= 6
    }

    private fun isValidPassword(): Boolean {
        return password.value.length >= 6
    }
}