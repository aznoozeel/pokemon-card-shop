package com.zoonza.pokemoncardshop.auth.internal.adapter.out.jwt

import com.zoonza.pokemoncardshop.auth.internal.application.port.out.AccessTokenGenerator
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class JwtAccessTokenGenerator(
    private val jwtEncoder: JwtEncoder,
    private val jwtProperties: JwtProperties,
) : AccessTokenGenerator {

    override fun generateAccessToken(memberId: Long): String {
        return generateAccessToken(memberId = memberId, issuedAt = Instant.now())
    }

    internal fun generateAccessToken(memberId: Long, issuedAt: Instant): String {
        return encode(
            memberId = memberId,
            tokenId = UUID.randomUUID().toString(),
            issuedAt = issuedAt,
            expiresAt = issuedAt.plus(jwtProperties.accessTtl),
        )
    }

    private fun encode(
        memberId: Long,
        tokenId: String,
        issuedAt: Instant,
        expiresAt: Instant,
    ): String {
        val headers = JwsHeader.with(MacAlgorithm.HS256).build()
        val claims = JwtClaimsSet.builder()
            .subject(memberId.toString())
            .id(tokenId)
            .issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(headers, claims)).tokenValue
    }
}
