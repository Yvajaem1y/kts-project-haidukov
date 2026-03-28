package org.example.project.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class RemoteLoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)

@Serializable
data class RemoteLoginResponse(
    @SerialName("status")
    val status: String,
    @SerialName("data")
    val data: RemoteUserData? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("message")
    val message: String? = null
)

@Serializable
data class RemoteUserData(
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("cabinets")
    val cabinets: List<RemoteCabinet>? = null
)

@Serializable
data class RemoteCabinet(
    @SerialName("_id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("domain")
    val domain: String,
    @SerialName("projects")
    val projects: List<RemoteProject>? = null
)

@Serializable
data class RemoteProject(
    @SerialName("_id")
    val id: String,
    @SerialName("name")
    val name: String
)

@Serializable
data class RemoteTokenResponse(
    @SerialName("token")
    val token: String,
    @SerialName("expires")
    val expires: String
)

@Serializable
data class ServerError(
    @SerialName("code")
    val code: String,
    @SerialName("status")
    val status: String,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: JsonObject? = null
)

@Serializable
data class ServerResponse<T>(
    @SerialName("status")
    val status: String,
    @SerialName("data")
    val data: T? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("message")
    val message: String? = null
)

@Serializable
data class RemoteMeResponse(
    @SerialName("data")
    val data: RemoteUserData,
    @SerialName("status")
    val status: String
)