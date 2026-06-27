package com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.security.oauth2

import com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.support.RefreshTokenCookieWriter
import com.zoonza.pokemoncardshop.auth.test.fake.FakeIssueTokenUseCase
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import java.time.Duration

class OAuth2SuccessHandlerTest {

    private val successRedirectUrl = "http://localhost:3000/auth/success"
    private val refreshTokenTtl = Duration.ofDays(7)
    private val issueTokenUseCase = FakeIssueTokenUseCase()
    private val oauth2SuccessHandler = OAuth2SuccessHandler(
        issueTokenUseCase = issueTokenUseCase,
        refreshTokenCookieWriter = RefreshTokenCookieWriter(refreshTokenTtl),
        successRedirectUrl = successRedirectUrl,
    )

    @Test
    fun `OAuth2 인증 성공 시 리프레시 토큰을 쿠키로 설정하고 액세스 토큰은 fragment 리다이렉트로 전달한다`() {
        val principal = CustomOidcUser(
            memberId = 1L,
            delegate = DefaultOidcUser(
                emptySet(),
                OidcIdToken(
                    "id-token-value",
                    null,
                    null,
                    mapOf("sub" to "google-social-id"),
                ),
            ),
        )
        val authentication = TestingAuthenticationToken(principal, null)
        val response = MockHttpServletResponse()

        oauth2SuccessHandler.onAuthenticationSuccess(
            MockHttpServletRequest(),
            response,
            authentication,
        )

        issueTokenUseCase.issuedCommands.single().memberId shouldBe 1L
        response.redirectedUrl shouldBe "$successRedirectUrl#access_token=access-token"
        response.redirectedUrl shouldStartWith successRedirectUrl
        response.getHeader("Set-Cookie") shouldContain "refresh_token=refresh-token"
        response.getHeader("Set-Cookie") shouldContain "Path=/api/auth"
        response.getHeader("Set-Cookie") shouldContain "Max-Age=${refreshTokenTtl.toSeconds()}"
    }

    @Test
    fun `OAuth2 인증 principal 타입이 올바르지 않으면 예외가 발생한다`() {
        val authentication = TestingAuthenticationToken("invalid-principal", null)
        val response = MockHttpServletResponse()

        shouldThrow<ClassCastException> {
            oauth2SuccessHandler.onAuthenticationSuccess(
                MockHttpServletRequest(),
                response,
                authentication,
            )
        }
    }
}
