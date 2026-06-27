package com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.security.oauth2

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class CustomOidcUser(
    val memberId: Long,
    private val delegate: OidcUser,
) : OidcUser {

    override fun getName(): String = memberId.toString()

    override fun getAttributes(): Map<String, Any> = delegate.attributes

    override fun getAuthorities(): Collection<GrantedAuthority> = delegate.authorities

    override fun getIdToken(): OidcIdToken = delegate.idToken

    override fun getUserInfo(): OidcUserInfo? = delegate.userInfo

    override fun getClaims(): Map<String, Any> = delegate.claims
}
