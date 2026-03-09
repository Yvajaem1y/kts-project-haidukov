package org.example.project.di

import com.russhwolf.settings.Settings
import com.russhwolf.settings.NSUserDefaultsSettings
import platform.Foundation.NSUserDefaults


actual fun createSettings(context: Any?): Settings {
    return NSUserDefaultsSettings(
        delegate = NSUserDefaults.standardUserDefaults
    )
}