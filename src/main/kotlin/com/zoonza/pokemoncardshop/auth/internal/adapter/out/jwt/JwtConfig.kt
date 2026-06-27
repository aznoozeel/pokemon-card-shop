package com.zoonza.pokemoncardshop.auth.internal.adapter.out.jwt

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.nio.charset.StandardCharsets
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class JwtConfig {

    @Bean
    fun jwtEncoder(jwtProperties: JwtProperties): JwtEncoder {
        val secretKey = secretKey(jwtProperties)

        return NimbusJwtEncoder.withSecretKey(secretKey)
            .algorithm(MacAlgorithm.HS256)
            .build()
    }

    @Bean
    fun jwtDecoder(jwtProperties: JwtProperties): JwtDecoder {
        val secretKey = secretKey(jwtProperties)

        return NimbusJwtDecoder.withSecretKey(secretKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build()
    }

    private fun secretKey(jwtProperties: JwtProperties): SecretKeySpec {
        val secretKey = SecretKeySpec(
            jwtProperties.secretKey.toByteArray(StandardCharsets.UTF_8),
            HMAC_SHA256
        )

        return secretKey
    }

    companion object {
        private const val HMAC_SHA256 = "HmacSHA256"
    }
}
