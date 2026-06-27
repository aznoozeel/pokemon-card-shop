package com.zoonza.pokemoncardshop.auth.internal.application.service

import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.*

@Component
class RefreshTokenGenerator {
    private val secureRandom = SecureRandom()

    fun generate(): String {
        val bytes = ByteArray(REFRESH_TOKEN_BYTES)
        secureRandom.nextBytes(bytes)

        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes)
    }

    companion object {
        private const val REFRESH_TOKEN_BYTES = 64
    }
}
