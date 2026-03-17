package org.example.project.oAuth.models

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String,
)