package org.example.project.oAuth.models

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.TokenRequest
import org.example.project.data.local_database.dataStore.DataStoreRepository

class AuthRepository(
    val dataStore : DataStoreRepository
) {

    fun corruptAccessToken() {
        TokenStorage.accessToken = "fake token"
    }

    suspend fun logout() {
        TokenStorage.accessToken = null
        TokenStorage.refreshToken = null
        TokenStorage.idToken = null

        dataStore.logOut()
    }

    fun getAuthRequest(): AuthorizationRequest {
        return AppAuth.getAuthRequest()
    }

    fun getEndSessionRequest(): EndSessionRequest {
        return AppAuth.getEndSessionRequest()
    }

    suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ) {
        val tokens : TokensModel = AppAuth.performTokenRequestSuspend(authService, tokenRequest)
        TokenStorage.accessToken = tokens.accessToken
        TokenStorage.refreshToken = tokens.refreshToken
        TokenStorage.idToken = tokens.idToken

        dataStore.authorizationSuccess(tokens = tokens)
    }
}