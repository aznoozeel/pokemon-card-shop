package com.zoonza.pokemoncardshop.auth.internal.domain

import com.zoonza.pokemoncardshop.common.error.DomainException

enum class OAuth2Provider(
    val registrationId: String
) {
    GOOGLE("google"),
    ;

    companion object {
        fun from(registrationId: String): OAuth2Provider =
            entries.find { it.registrationId == registrationId }
                ?: throw DomainException(SocialAccountErrorCode.UNSUPPORTED_OAUTH_PROVIDER)
    }
}