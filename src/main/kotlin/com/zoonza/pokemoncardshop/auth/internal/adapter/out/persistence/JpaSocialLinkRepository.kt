package com.zoonza.pokemoncardshop.auth.internal.adapter.out.persistence

import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialLink
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialLinkRepository
import org.springframework.stereotype.Repository

@Repository
class JpaSocialLinkRepository(
    private val socialLinkJpaRepository: SocialLinkJpaRepository
) : SocialLinkRepository {

    override fun findByProviderAndSocialId(provider: OAuth2Provider, socialId: String): SocialLink? {
        return socialLinkJpaRepository.findByProviderAndSocialId(provider, socialId)
    }

    override fun save(socialLink: SocialLink): SocialLink {
        return socialLinkJpaRepository.save(socialLink)
    }
}
