package org.example.project.data.local_database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.example.project.data.local_database.dataStore.createDataStore
import org.example.project.data.local_database.dataStore.dataStoreFileName

fun createDataStoreAndroid(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)