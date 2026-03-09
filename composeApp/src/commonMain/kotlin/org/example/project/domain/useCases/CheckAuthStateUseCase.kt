package org.example.project.domain.useCases

import org.example.project.data.repository.AuthRepository
import org.example.project.domain.models.AuthState

class CheckAuthStateUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): AuthState {
        return authRepository.checkAuthState()
    }
}