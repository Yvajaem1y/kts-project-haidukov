package org.example.project.di

import org.koin.core.module.Module

actual val platformModule: Module = module {
    single { AuthRepository() }
}