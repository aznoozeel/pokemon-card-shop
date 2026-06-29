package com.zoonza.pokemoncardshop.auth.internal.application.service

import com.zoonza.pokemoncardshop.auth.internal.adapter.out.jwt.JwtAccessTokenGenerator
import com.zoonza.pokemoncardshop.auth.internal.adapter.out.jwt.JwtConfig
import com.zoonza.pokemoncardshop.auth.internal.adapter.out.jwt.JwtProperties
import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.IssueTokenCommand
import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.ReissueTokenCommand
import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.RevokeRefreshTokenCommand
import com.zoonza.pokemoncardshop.auth.test.fake.FakeRefreshTokenStore
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration

class TokenServiceTest {
    private lateinit var jwtProperties: JwtProperties
    private lateinit var jwtTokenProvider: JwtAccessTokenGenerator
    private lateinit var refreshTokenRepository: FakeRefreshTokenStore
    private lateinit var tokenService: TokenService

    @BeforeEach
    fun setUp() {
        jwtProperties = JwtProperties(
            secretKey = "test-secret-key-for-hs256-token-signing",
            accessTtl = Duration.ofMillis(1_800_000L),
        )
        jwtTokenProvider = JwtAccessTokenGenerator(
            jwtEncoder = JwtConfig().jwtEncoder(jwtProperties),
            jwtProperties = jwtProperties,
        )
        refreshTokenRepository = FakeRefreshTokenStore()
        tokenService = TokenService(
            accessTokenGenerator = jwtTokenProvider,
            refreshTokenStore = refreshTokenRepository,
            refreshTokenGenerator = RefreshTokenGenerator(),
            refreshTokenTtl = Duration.ofMillis(604_800_000L),
        )
    }

    @Test
    fun `토큰 발급 시 리프레시 토큰을 저장한다`() {
        val result = tokenService.issue(IssueTokenCommand(memberId = 1L))

        result.memberId shouldBe 1L
        refreshTokenRepository.savedRefreshTokens[result.refreshToken] shouldBe 1L
    }

    @Test
    fun `재발급 시 기존 리프레시 토큰을 삭제하고 새 토큰을 저장한다`() {
        val issuedToken = tokenService.issue(IssueTokenCommand(memberId = 1L))

        val reissuedToken = tokenService.reissue(
            ReissueTokenCommand(issuedToken.refreshToken)
        )

        reissuedToken.memberId shouldBe 1L
        refreshTokenRepository.deletedRefreshTokens.shouldContainExactly(issuedToken.refreshToken)
        refreshTokenRepository.savedRefreshTokens.containsKey(issuedToken.refreshToken) shouldBe false
        refreshTokenRepository.savedRefreshTokens.containsKey(reissuedToken.refreshToken) shouldBe true
    }

    @Test
    fun `로그아웃 시 리프레시 토큰을 삭제한다`() {
        val issuedToken = tokenService.issue(IssueTokenCommand(memberId = 1L))

        tokenService.revoke(RevokeRefreshTokenCommand(issuedToken.refreshToken))

        refreshTokenRepository.deletedRefreshTokens.shouldContainExactly(issuedToken.refreshToken)
        refreshTokenRepository.savedRefreshTokens.containsKey(issuedToken.refreshToken) shouldBe false
    }
}
