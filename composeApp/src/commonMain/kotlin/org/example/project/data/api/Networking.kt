package org.example.project.data.api

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.cookies.ConstantCookiesStorage
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object Networking {

    private val cookieStorage = ConstantCookiesStorage()

    fun createHttpClient(
        domain: String,
        cabinetId: String? = null,
        projectId: String? = null
    ): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = false
                })
            }

            install(HttpCookies) {
                storage = cookieStorage
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d(message, tag = "Ktor")
                    }
                }
                level = LogLevel.HEADERS
            }

            defaultRequest {
                url("https://$domain.smartbotpro.ru/")
                contentType(ContentType.Application.Json)

                cabinetId?.let {
                    header("X-SPro-Cabinet", it)
                }
                projectId?.let {
                    header("X-SPro-Project", it)
                }
            }
        }
    }

    fun clearCookies() {
        cookieStorage.close()
    }
}