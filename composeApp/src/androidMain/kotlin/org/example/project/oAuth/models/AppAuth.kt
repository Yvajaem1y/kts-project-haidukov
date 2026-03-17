package org.example.project.oAuth.models

import android.net.Uri
import androidx.core.net.toUri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest
import kotlin.coroutines.suspendCoroutine

object AppAuth {

    private val serviceConfiguration = AuthorizationServiceConfiguration(
        AuthConfig.AUTH_URI.toUri(),
        AuthConfig.TOKEN_URI.toUri(),
        null, // registration endpoint
        AuthConfig.END_SESSION_URI.toUri()
    )

    fun getAuthRequest(): AuthorizationRequest {
        val redirectUri = AuthConfig.CALLBACK_URL.toUri()

        return AuthorizationRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            redirectUri
        )
            .setScope(AuthConfig.SCOPE)
            .build()
    }

    fun getEndSessionRequest(): EndSessionRequest {
        return EndSessionRequest.Builder(serviceConfiguration)
            .setPostLogoutRedirectUri(AuthConfig.LOGOUT_CALLBACK_URL.toUri())
            .build()
    }

    fun getRefreshTokenRequest(refreshToken: String): TokenRequest {
        return TokenRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID
        )
            .setGrantType(GrantTypeValues.REFRESH_TOKEN)
            .setScopes(AuthConfig.SCOPE)
            .setRefreshToken(refreshToken)
            .build()
    }

    suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ): TokensModel {
        return suspendCoroutine { continuation ->
            authService.performTokenRequest(
                tokenRequest,
                getClientAuthentication()
            ) { response, ex ->
                when {
                    response != null -> {
                        val tokens = TokensModel(
                            accessToken = response.accessToken.orEmpty(),
                            refreshToken = response.refreshToken.orEmpty(),
                            idToken = response.idToken.orEmpty()
                        )
                        continuation.resumeWith(Result.success(tokens))
                    }

                    ex != null -> {
                        continuation.resumeWith(Result.failure(ex))
                    }

                    else -> error("unreachable")
                }
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }
}