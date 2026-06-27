package com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.support

import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RefreshTokenCookieWriter(
    @Value("\${refresh-token.ttl}")
    private val refreshTokenTtl: Duration,
) {
    fun set(response: HttpServletResponse, refreshToken: String) {
        response.addHeader(SET_COOKIE, build(refreshToken, refreshTokenTtl).toString())
    }

    fun clear(response: HttpServletResponse) {
        response.addHeader(SET_COOKIE, build("", Duration.ZERO).toString())
    }

    private fun build(value: String, maxAge: Duration): ResponseCookie {
        return ResponseCookie.from(NAME, value)
            .httpOnly(true)
            .secure(false)
            .sameSite(SAME_SITE)
            .path(PATH)
            .maxAge(maxAge)
            .build()
    }

    companion object {
        const val NAME = "refresh_token"
        private const val SET_COOKIE = "Set-Cookie"
        private const val PATH = "/api/auth"
        private const val SAME_SITE = "Lax"
    }
}