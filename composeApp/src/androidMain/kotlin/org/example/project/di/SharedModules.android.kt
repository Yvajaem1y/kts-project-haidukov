package org.example.project.di

import com.russhwolf.settings.Settings
import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings

actual fun createSettings(context: Any?): Settings {
    context as Context
    return SharedPreferencesSettings(
        delegate = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    )
}