package com.zoonza.pokemoncardshop.auth.internal.application.dto.result

data class TokenResult(
    val memberId: Long,
    val accessToken: String,
    val refreshToken: String,
)
