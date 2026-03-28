package org.example.project.presentation.mockDatabase

data class User(
    val username: String = "",
    val userFirstName: String = "",
    val userLastName: String = "",
    val isUserOnline: Boolean = false,
    val lastUserTimeOnline: String? = null,
    val userAvatar: String? = null
)