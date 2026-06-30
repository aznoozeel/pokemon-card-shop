package com.zoonza.pokemoncardshop.auth.internal.domain

interface SocialLinkRepository {
    fun findByProviderAndSocialId(provider: OAuth2Provider, socialId: String): SocialLink?

    fun save(socialLink: SocialLink): SocialLink
}
