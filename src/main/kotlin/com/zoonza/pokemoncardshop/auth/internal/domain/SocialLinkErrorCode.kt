package com.zoonza.pokemoncardshop.auth.internal.domain

import com.zoonza.pokemoncardshop.common.error.ErrorCode
import com.zoonza.pokemoncardshop.common.error.ErrorStatus

enum class SocialLinkErrorCode(
    override val status: ErrorStatus,
    override val message: String,
) : ErrorCode {
    UNSUPPORTED_OAUTH_PROVIDER(ErrorStatus.BAD_REQUEST, "지원하지 않는 소셜 로그인 서비스입니다."),
    INVALID_OAUTH_USER_INFO(ErrorStatus.UNAUTHORIZED, "소셜 로그인 사용자 정보를 확인할 수 없습니다."),
    ;

    override val code = "SOCIAL_ACCOUNT_$name"
}