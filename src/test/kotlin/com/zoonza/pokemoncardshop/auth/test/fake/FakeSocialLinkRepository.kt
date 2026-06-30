package com.zoonza.pokemoncardshop.auth.test.fake

import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialLink
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialLinkRepository

class FakeSocialLinkRepository : SocialLinkRepository {
    private var nextId = 1L
    private val socialLinks = mutableListOf<SocialLink>()

    val savedSocialLinks = mutableListOf<SocialLink>()

    fun addSocialAccounts(vararg socialLinks: SocialLink) {
        this.socialLinks.addAll(socialLinks)
    }

    override fun findByProviderAndSocialId(provider: OAuth2Provider, socialId: String): SocialLink? {
        return socialLinks.find { it.provider == provider && it.socialId == socialId }
    }

    override fun save(socialLink: SocialLink): SocialLink {
        val idField = socialLink::class.java.getDeclaredField("id")
        idField.isAccessible = true
        idField.set(socialLink, nextId++)

        socialLinks.add(socialLink)
        savedSocialLinks.add(socialLink)

        return socialLink
    }
}
