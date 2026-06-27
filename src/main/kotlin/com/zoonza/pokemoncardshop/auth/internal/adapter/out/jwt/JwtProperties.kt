package com.zoonza.pokemoncardshop.auth.internal.adapter.out.jwt

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.nio.charset.StandardCharsets
import java.time.Duration

@Validated
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    @field:NotBlank(message = "JWT 시크릿 키는 필수입니다.")
    val secretKey: String,

    @field:NotNull(message = "JWT 액세스 토큰 만료 시간은 필수입니다.")
    val accessTtl: Duration,
) {
    init {
        require(secretKey.toByteArray(StandardCharsets.UTF_8).size >= MIN_HMAC_SECRET_BYTES) {
            "JWT 시크릿 키는 최소 32바이트 이상이어야 합니다."
        }
        require(accessTtl.isPositive) {
            "JWT 액세스 토큰 만료 시간은 양수여야 합니다."
        }
    }

    companion object {
        private const val MIN_HMAC_SECRET_BYTES = 32
    }
}