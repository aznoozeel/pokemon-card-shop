package com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.web.dto.response

import com.zoonza.pokemoncardshop.auth.internal.application.dto.result.TokenResult

data class TokenResponse(
    val memberId: Long,
    val tokenType: String = "Bearer",
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        fun from(result: TokenResult): TokenResponse {
            return TokenResponse(
                memberId = result.memberId,
                accessToken = result.accessToken,
                refreshToken = result.refreshToken,
            )
        }
    }
}