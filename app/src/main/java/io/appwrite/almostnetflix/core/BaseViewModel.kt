package io.appwrite.almostnetflix.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    private val _message = MutableLiveData<Message>()
    val message: LiveData<Message> = _message

    private val _isBusy = MutableLiveData<Boolean>()
    val isBusy: LiveData<Boolean> = _isBusy

    fun setBusy(busy: Boolean) {
        _isBusy.postValue(busy)
    }

    fun postMessage(msg: Messages, extras: List<Any>? = null) {
        _message.postValue(Message(msg, extras))
    }
}