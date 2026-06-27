package com.zoonza.pokemoncardshop.auth.internal.domain

interface SocialAccountRepository {
    fun findByProviderAndSocialId(provider: OAuth2Provider, socialId: String): SocialAccount?

    fun save(socialAccount: SocialAccount): SocialAccount
}
