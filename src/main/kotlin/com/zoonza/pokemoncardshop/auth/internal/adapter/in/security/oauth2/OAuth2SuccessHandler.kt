package com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.security.oauth2

import com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.support.RefreshTokenCookieWriter
import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.IssueTokenCommand
import com.zoonza.pokemoncardshop.auth.internal.application.port.`in`.IssueTokenUseCase
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private const val ACCESS_TOKEN_FRAGMENT_KEY = "access_token"

@Component
class OAuth2SuccessHandler(
    private val issueTokenUseCase: IssueTokenUseCase,
    private val refreshTokenCookieWriter: RefreshTokenCookieWriter,
    @Value("\${app.oauth2.success-redirect-url}")
    private val successRedirectUrl: String,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val principal = authentication.principal as CustomOidcUser
        val tokenResult = issueTokenUseCase.issue(IssueTokenCommand(principal.memberId))

        refreshTokenCookieWriter.set(response, tokenResult.refreshToken)

        redirectToFrontend(response, tokenResult.accessToken)
    }

    private fun redirectToFrontend(response: HttpServletResponse, accessToken: String) {
        val encodedAccessToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
        val redirectLocation = "$successRedirectUrl#$ACCESS_TOKEN_FRAGMENT_KEY=$encodedAccessToken"

        response.sendRedirect(redirectLocation)
    }
}