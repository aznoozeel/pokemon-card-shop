package com.zoonza.pokemoncardshop.auth.internal.domain

import com.zoonza.pokemoncardshop.common.error.ErrorCode
import com.zoonza.pokemoncardshop.common.error.ErrorStatus

enum class AuthErrorCode(
    override val status: ErrorStatus,
    override val message: String,
) : ErrorCode {
    INVALID_OAUTH2_AUTHENTICATION(ErrorStatus.UNAUTHORIZED, "OAuth2 인증 사용자 정보가 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(ErrorStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(ErrorStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    ;

    override val code = "AUTH_$name"
}
