package io.appwrite.almostnetflix.core

data class Message(
    val type: Messages,
    val extras: List<Any>? = null,
)