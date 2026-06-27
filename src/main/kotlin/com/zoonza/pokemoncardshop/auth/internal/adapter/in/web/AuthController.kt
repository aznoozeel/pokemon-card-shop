package com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.web

import com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.support.RefreshTokenCookieWriter
import com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.web.dto.response.TokenResponse
import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.ReissueTokenCommand
import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.RevokeRefreshTokenCommand
import com.zoonza.pokemoncardshop.auth.internal.application.port.`in`.ReissueTokenUseCase
import com.zoonza.pokemoncardshop.auth.internal.application.port.`in`.RevokeRefreshTokenUseCase
import com.zoonza.pokemoncardshop.auth.internal.domain.AuthErrorCode
import com.zoonza.pokemoncardshop.common.error.DomainException
import com.zoonza.pokemoncardshop.common.response.ApiResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val reissueTokenUseCase: ReissueTokenUseCase,
    private val revokeRefreshTokenUseCase: RevokeRefreshTokenUseCase,
    private val refreshTokenCookieWriter: RefreshTokenCookieWriter,
) {

    @PostMapping("/token/reissue")
    fun reissue(
        @CookieValue(name = RefreshTokenCookieWriter.NAME, required = false) refreshToken: String?,
        response: HttpServletResponse,
    ): ApiResponse<TokenResponse> {
        val token = requireRefreshToken(refreshToken)
        val result = reissueTokenUseCase.reissue(ReissueTokenCommand(token))

        refreshTokenCookieWriter.set(response, result.refreshToken)

        return ApiResponse.success(TokenResponse.from(result))
    }

    @PostMapping("/logout")
    fun logout(
        @CookieValue(name = RefreshTokenCookieWriter.NAME, required = false) refreshToken: String?,
        response: HttpServletResponse,
    ): ApiResponse<Unit> {
        val token = requireRefreshToken(refreshToken)

        revokeRefreshTokenUseCase.revoke(RevokeRefreshTokenCommand(token))

        refreshTokenCookieWriter.clear(response)

        return ApiResponse.success()
    }

    private fun requireRefreshToken(refreshToken: String?): String {
        if (refreshToken.isNullOrBlank()) {
            throw DomainException(AuthErrorCode.INVALID_REFRESH_TOKEN)
        }

        return refreshToken
    }
}