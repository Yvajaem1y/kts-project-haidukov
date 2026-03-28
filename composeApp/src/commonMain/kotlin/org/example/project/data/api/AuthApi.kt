package org.example.project.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.project.data.models.RemoteCabinet
import org.example.project.data.models.RemoteLoginRequest
import org.example.project.data.models.RemoteLoginResponse
import org.example.project.data.models.RemoteMeResponse
import org.example.project.data.models.RemoteProject
import org.example.project.data.models.RemoteTokenResponse
import org.example.project.data.models.ServerResponse

class AuthApi(private val httpClient: HttpClient) {

    suspend fun login(domain: String, request: RemoteLoginRequest): RemoteLoginResponse {
        return httpClient.post("https://smartbotpro.ru/api/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun logout(domain: String): Unit {
        httpClient.post("https://$domain.smartbotpro.ru/api/logout")
    }

    suspend fun getMe(domain: String): RemoteMeResponse {
        return httpClient.get("https://$domain.smartbotpro.ru/api/me").body()
    }

    suspend fun getSubscriptionToken(domain: String): ServerResponse<RemoteTokenResponse> {
        return httpClient.get("https://$domain.smartbotpro.ru/api/conversations/obtain_subscription_token")
            .body()
    }
}

class CabinetApi(private val httpClient: HttpClient) {

    suspend fun getCabinets(domain: String): ServerResponse<List<RemoteCabinet>> {
        return httpClient.get("https://$domain.smartbotpro.ru/api/cabinets").body()
    }

    suspend fun getProjects(
        domain: String,
        cabinetId: String
    ): ServerResponse<List<RemoteProject>> {
        return httpClient.get("https://$domain.smartbotpro.ru/api/cabinets/$cabinetId/projects") {
            header("X-SPro-Cabinet", cabinetId)
        }.body()
    }
}