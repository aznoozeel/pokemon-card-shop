package com.zoonza.pokemoncardshop.auth.internal.adapter.out.jwt

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.crypto.spec.SecretKeySpec

class JwtAccessTokenGeneratorTest {

    private val jwtProperties = JwtProperties(
        secretKey = SECRET_KEY,
        accessTtl = Duration.ofMillis(ACCESS_TTL_MS),
    )
    private val jwtTokenProvider = JwtAccessTokenGenerator(
        jwtEncoder = JwtConfig().jwtEncoder(jwtProperties),
        jwtProperties = jwtProperties,
    )
    private val jwtDecoder = NimbusJwtDecoder.withSecretKey(
        SecretKeySpec(SECRET_KEY.toByteArray(StandardCharsets.UTF_8), HMAC_SHA256)
    )
        .macAlgorithm(MacAlgorithm.HS256)
        .build()

    @Test
    fun `액세스 토큰 생성 시 회원 ID를 subject로 설정한다`() {
        val issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS)

        val token = jwtTokenProvider.generateAccessToken(memberId = 1L, issuedAt = issuedAt)

        val accessToken = jwtDecoder.decode(token)
        accessToken.subject shouldBe "1"
        accessToken.id?.isNotBlank() shouldBe true
        accessToken.issuedAt shouldBe issuedAt
        accessToken.expiresAt shouldBe issuedAt.plusMillis(ACCESS_TTL_MS)
    }

    companion object {
        private const val SECRET_KEY = "test-secret-key-for-hs256-token-signing"
        private const val HMAC_SHA256 = "HmacSHA256"
        private const val ACCESS_TTL_MS = 1_800_000L
    }
}
