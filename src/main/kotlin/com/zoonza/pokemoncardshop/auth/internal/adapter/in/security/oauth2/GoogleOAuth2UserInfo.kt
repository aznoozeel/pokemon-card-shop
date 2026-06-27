package com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.security.oauth2

import com.zoonza.pokemoncardshop.auth.internal.domain.SocialAccountErrorCode
import com.zoonza.pokemoncardshop.common.error.DomainException

class GoogleOAuth2UserInfo(
    private val attributes: Map<String, Any>
) : OAuth2UserInfo {
    override val socialId: String
        get() = attributes["sub"] as? String
            ?: throw DomainException(SocialAccountErrorCode.INVALID_OAUTH_USER_INFO)
}
