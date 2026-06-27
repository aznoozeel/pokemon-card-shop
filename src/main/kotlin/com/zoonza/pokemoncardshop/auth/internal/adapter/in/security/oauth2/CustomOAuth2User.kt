package com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.security.oauth2

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    val memberId: Long,
    private val delegate: OAuth2User
) : OAuth2User {

    override fun getName(): String {
        return memberId.toString()
    }

    override fun getAttributes(): Map<String, Any> {
        return delegate.attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return delegate.authorities
    }
}