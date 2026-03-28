package org.example.project.domain.useCases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.example.project.data.repository.AuthRepository
import org.example.project.domain.models.User

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        domain: String,
        email: String,
        password: String
    ): Flow<Result<User>> = flow {
        emit(authRepository.login(domain, email, password))
    }
}