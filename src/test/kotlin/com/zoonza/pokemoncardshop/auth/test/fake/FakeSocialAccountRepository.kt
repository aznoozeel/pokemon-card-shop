package com.zoonza.pokemoncardshop.auth.test.fake

import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialAccount
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialAccountRepository

class FakeSocialAccountRepository : SocialAccountRepository {
    private var nextId = 1L
    private val socialAccounts = mutableListOf<SocialAccount>()

    val savedSocialAccounts = mutableListOf<SocialAccount>()

    fun addSocialAccounts(vararg socialAccounts: SocialAccount) {
        this.socialAccounts.addAll(socialAccounts)
    }

    override fun findByProviderAndSocialId(provider: OAuth2Provider, socialId: String): SocialAccount? {
        return socialAccounts.find { it.provider == provider && it.socialId == socialId }
    }

    override fun save(socialAccount: SocialAccount): SocialAccount {
        val idField = socialAccount::class.java.getDeclaredField("id")
        idField.isAccessible = true
        idField.set(socialAccount, nextId++)

        socialAccounts.add(socialAccount)
        savedSocialAccounts.add(socialAccount)

        return socialAccount
    }
}
