package org.example.project.di

import androidx.lifecycle.AndroidViewModel
import net.openid.appauth.AuthorizationService
import org.example.project.presentation.loginScreen.AndroidLoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

actual val platformModule: Module = module {
    viewModel<AndroidLoginViewModel> { AndroidLoginViewModel(AuthorizationService(get())) }
}