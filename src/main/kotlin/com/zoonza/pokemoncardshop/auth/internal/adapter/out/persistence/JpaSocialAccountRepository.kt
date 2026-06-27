package com.zoonza.pokemoncardshop.auth.internal.adapter.out.persistence

import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialAccount
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialAccountRepository
import org.springframework.stereotype.Repository

@Repository
class JpaSocialAccountRepository(
    private val socialAccountJpaRepository: SocialAccountJpaRepository
) : SocialAccountRepository {

    override fun findByProviderAndSocialId(provider: OAuth2Provider, socialId: String): SocialAccount? {
        return socialAccountJpaRepository.findByProviderAndSocialId(provider, socialId)
    }

    override fun save(socialAccount: SocialAccount): SocialAccount {
        return socialAccountJpaRepository.save(socialAccount)
    }
}
