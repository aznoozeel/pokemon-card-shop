package com.zoonza.pokemoncardshop.auth.internal.adapter.out.persistence

import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialAccount
import org.springframework.data.jpa.repository.JpaRepository

interface SocialAccountJpaRepository : JpaRepository<SocialAccount, Long> {
    fun findByProviderAndSocialId(provider: OAuth2Provider, socialId: String): SocialAccount?
}