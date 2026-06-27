package com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.security.oauth2

import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider

object OAuth2UserInfoMapper {
    fun map(provider: OAuth2Provider, attributes: Map<String, Any>): OAuth2UserInfo =
        when (provider) {
            OAuth2Provider.GOOGLE -> GoogleOAuth2UserInfo(attributes)
        }
}
