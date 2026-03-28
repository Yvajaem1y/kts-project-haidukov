package org.example.project.domain.models

import kotlin.time.Clock
import kotlin.time.Instant

data class User(
    val id: String,
    val email: String,
    val name: String,
    val cabinets: List<Cabinet> = emptyList()
)

data class Cabinet(
    val id: String,
    val name: String,
    val domain: String,
    val projects: List<Project> = emptyList()
)

data class Project(
    val id: String,
    val name: String,
    val cabinetId: String
)

sealed class AuthState {
    object Unauthorized : AuthState()
    object Authorized : AuthState()
    data class Loading(val isLoading: Boolean = true) : AuthState()
    data class Error(val message: String) : AuthState()
}

data class SessionInfo(
    val domain: String,
    val cabinetId: String? = null,
    val projectId: String? = null,
    val expiresAt: Instant? = null
) {
    val isValid: Boolean
        get() = expiresAt?.let { it > Clock.System.now() } ?: true
}