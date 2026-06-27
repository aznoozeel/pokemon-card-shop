package com.zoonza.pokemoncardshop.auth.internal.application.dto.command

data class RevokeRefreshTokenCommand(
    val refreshToken: String,
)
