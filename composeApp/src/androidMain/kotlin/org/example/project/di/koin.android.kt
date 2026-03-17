package org.example.project.di

import net.openid.appauth.AuthorizationService
import org.example.project.data.local_database.createDataStoreAndroid
import org.example.project.data.local_database.dataStore.DataStoreRepository
import org.example.project.data.local_database.room.AppDatabase
import org.example.project.data.local_database.getDatabase
import org.example.project.presentation.authorizationScreen.AndroidAuthorizationViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

actual val platformModule: Module = module {
    viewModel<AndroidAuthorizationViewModel> { AndroidAuthorizationViewModel(authService = AuthorizationService(get()), dataStoreRepository = get()) }

    single <DataStoreRepository> { DataStoreRepository(createDataStoreAndroid(get())) }
    single<AppDatabase> { getDatabase(get()) }
}