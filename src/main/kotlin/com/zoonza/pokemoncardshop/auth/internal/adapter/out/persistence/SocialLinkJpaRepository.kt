package com.zoonza.pokemoncardshop.auth.internal.adapter.out.persistence

import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialLink
import org.springframework.data.jpa.repository.JpaRepository

interface SocialLinkJpaRepository : JpaRepository<SocialLink, Long> {
    fun findByProviderAndSocialId(provider: OAuth2Provider, socialId: String): SocialLink?
}