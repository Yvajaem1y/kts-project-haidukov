package org.example.project.di

import com.russhwolf.settings.Settings
import org.example.project.data.api.AuthApi
import org.example.project.data.api.CabinetApi
import org.example.project.data.api.Networking
import org.example.project.data.repository.AuthRepository
import org.example.project.domain.useCases.CheckAuthStateUseCase
import org.example.project.domain.useCases.LoginUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedModule = module {

    single<Settings> { createSettings() }

    single<AuthRepository> { AuthRepository(get()) }

    factory<LoginUseCase> { LoginUseCase(get()) }
    factory<CheckAuthStateUseCase> { CheckAuthStateUseCase(get()) }

    factory { (domain: String) ->
        val httpClient = Networking.createHttpClient(domain)
        AuthApi(httpClient)
    }

    factory { (domain: String) ->
        val httpClient = Networking.createHttpClient(domain)
        CabinetApi(httpClient)
    }
}

expect fun createSettings(context: Any? = null): Settings