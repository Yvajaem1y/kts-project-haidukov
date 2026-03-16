package org.example.project.di

import net.openid.appauth.AuthorizationService
import org.example.project.data.local_database.AppDatabase
import org.example.project.data.local_database.getDatabase
import org.example.project.presentation.loginScreen.AndroidLoginViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

actual val platformModule: Module = module {
    viewModel<AndroidLoginViewModel> { AndroidLoginViewModel(AuthorizationService(get())) }
    single<AppDatabase> { getDatabase(get()) }
}