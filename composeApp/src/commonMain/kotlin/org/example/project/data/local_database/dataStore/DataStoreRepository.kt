package org.example.project.data.local_database.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.local_database.dataStore.DataStoreRepository.PreferenceKeys.AUTHORIZATION_ACCESS_TOKEN
import org.example.project.data.local_database.dataStore.DataStoreRepository.PreferenceKeys.AUTHORIZATION_ID_TOKEN
import org.example.project.data.local_database.dataStore.DataStoreRepository.PreferenceKeys.AUTHORIZATION_REFRESH_TOKEN
import org.example.project.data.local_database.dataStore.DataStoreRepository.PreferenceKeys.IS_FIRST_TIME_IN_APP
import org.example.project.oAuth.models.TokensModel


class DataStoreRepository(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferenceKeys {
        val IS_FIRST_TIME_IN_APP = booleanPreferencesKey("is_first_time_in_app")
        val AUTHORIZATION_ACCESS_TOKEN = stringPreferencesKey("authorization_access_token")
        val AUTHORIZATION_REFRESH_TOKEN = stringPreferencesKey("authorization_refresh_token")
        val AUTHORIZATION_ID_TOKEN = stringPreferencesKey("authorization_id_token")
    }

    fun getIsFirstTimeInApp(): Flow<Boolean> =
        dataStore.data.map { preferences -> preferences[IS_FIRST_TIME_IN_APP] ?: true }

    fun getIsLoggedIn(): Flow<Boolean> = dataStore.data.map { preferences ->
        (preferences[AUTHORIZATION_ACCESS_TOKEN] ?: "").isNotEmpty() &&
                (preferences[AUTHORIZATION_REFRESH_TOKEN] ?: "").isNotEmpty() &&
                (preferences[AUTHORIZATION_ID_TOKEN] ?: "").isNotEmpty()
    }

    suspend fun authorizationSuccess(tokens: TokensModel) {
        dataStore.edit { preferences ->
            tokens.accessToken.let {
                preferences[AUTHORIZATION_ACCESS_TOKEN] = it
            }
            tokens.refreshToken.let {
                preferences[AUTHORIZATION_REFRESH_TOKEN] = it
            }
            tokens.idToken.let {
                preferences[AUTHORIZATION_ID_TOKEN] = it
            }
        }
    }

    suspend fun logOut() =
        dataStore.edit { preferences -> preferences[AUTHORIZATION_ACCESS_TOKEN] = "" }

    suspend fun onBoardingIsShown() =
        dataStore.edit { preferences -> preferences[IS_FIRST_TIME_IN_APP] = false }
}