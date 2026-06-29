package com.zoonza.pokemoncardshop.auth.internal.application.service

import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.IssueTokenCommand
import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.ReissueTokenCommand
import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.RevokeRefreshTokenCommand
import com.zoonza.pokemoncardshop.auth.internal.application.dto.result.TokenResult
import com.zoonza.pokemoncardshop.auth.internal.application.port.`in`.IssueTokenUseCase
import com.zoonza.pokemoncardshop.auth.internal.application.port.`in`.ReissueTokenUseCase
import com.zoonza.pokemoncardshop.auth.internal.application.port.`in`.RevokeRefreshTokenUseCase
import com.zoonza.pokemoncardshop.auth.internal.application.port.out.AccessTokenGenerator
import com.zoonza.pokemoncardshop.auth.internal.application.port.out.RefreshTokenStore
import com.zoonza.pokemoncardshop.auth.internal.domain.AuthErrorCode
import com.zoonza.pokemoncardshop.common.error.DomainException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class TokenService(
    private val accessTokenGenerator: AccessTokenGenerator,
    private val refreshTokenGenerator: RefreshTokenGenerator,
    private val refreshTokenStore: RefreshTokenStore,
    @Value("\${refresh-token.ttl}")
    private val refreshTokenTtl: Duration,
) : IssueTokenUseCase, ReissueTokenUseCase, RevokeRefreshTokenUseCase {

    override fun issue(command: IssueTokenCommand): TokenResult {
        return issue(command.memberId)
    }

    override fun reissue(command: ReissueTokenCommand): TokenResult {
        val memberId = refreshTokenStore.findMemberIdByRefreshToken(command.refreshToken)
            ?: throw DomainException(AuthErrorCode.EXPIRED_REFRESH_TOKEN)

        refreshTokenStore.delete(command.refreshToken)

        return issue(memberId)
    }

    override fun revoke(command: RevokeRefreshTokenCommand) {
        refreshTokenStore.delete(command.refreshToken)
    }

    private fun issue(memberId: Long): TokenResult {
        val accessToken = accessTokenGenerator.generateAccessToken(memberId)
        val refreshToken = refreshTokenGenerator.generate()

        refreshTokenStore.save(
            refreshToken = refreshToken,
            memberId = memberId,
            ttl = refreshTokenTtl,
        )

        return TokenResult(
            memberId = memberId,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
