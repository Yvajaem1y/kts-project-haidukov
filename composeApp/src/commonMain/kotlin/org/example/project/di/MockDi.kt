package org.example.project.di

import androidx.compose.ui.input.key.Key.Companion.Settings
import com.russhwolf.settings.Settings
import org.example.project.data.repository.AuthRepository
import org.example.project.domain.useCases.CheckAuthStateUseCase
import org.example.project.domain.useCases.LoginUseCase

object MockDi {

    private var context: Any? = null

    fun setContext(context: Any?) {
        this.context = context

    }

    private val settings: Settings by lazy {
        context?.let { context ->
            createSettings(context)
        } ?: createSettings()
    }

    private val authRepository: AuthRepository by lazy {
        AuthRepository(settings)
    }

    val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(authRepository)
    }

    val checkAuthStateUseCase: CheckAuthStateUseCase by lazy {
        CheckAuthStateUseCase(authRepository)
    }
}