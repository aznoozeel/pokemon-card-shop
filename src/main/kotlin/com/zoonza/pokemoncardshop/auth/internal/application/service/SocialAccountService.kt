package com.zoonza.pokemoncardshop.auth.internal.application.service

import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.SocialLoginCommand
import com.zoonza.pokemoncardshop.auth.internal.application.dto.result.SocialLoginResult
import com.zoonza.pokemoncardshop.auth.internal.application.port.`in`.SocialLoginUseCase
import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialAccount
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialAccountRepository
import com.zoonza.pokemoncardshop.member.api.MemberRegistrationApi
import com.zoonza.pokemoncardshop.member.api.dto.command.RegisterMemberCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SocialAccountService(
    private val memberRegistrationApi: MemberRegistrationApi,
    private val socialAccountRepository: SocialAccountRepository
) : SocialLoginUseCase {

    @Transactional
    override fun login(command: SocialLoginCommand): SocialLoginResult {
        val loggedInAt = LocalDateTime.now()

        val socialAccount = findOrRegisterSocialAccount(
            provider = command.provider,
            socialId = command.socialId,
            loggedInAt = loggedInAt
        )

        socialAccount.login(loggedInAt)

        return SocialLoginResult(socialAccount.memberId)
    }

    private fun findOrRegisterSocialAccount(
        provider: OAuth2Provider,
        socialId: String,
        loggedInAt: LocalDateTime
    ): SocialAccount {
        return socialAccountRepository.findByProviderAndSocialId(
            provider = provider,
            socialId = socialId
        ) ?: registerSocialAccount(
            provider = provider,
            socialId = socialId,
            registeredAt = loggedInAt
        )
    }

    private fun registerSocialAccount(
        provider: OAuth2Provider,
        socialId: String,
        registeredAt: LocalDateTime
    ): SocialAccount {
        val result = memberRegistrationApi.register(
            RegisterMemberCommand(registeredAt)
        )

        val socialAccount = SocialAccount.register(
            memberId = result.memberId,
            provider = provider,
            socialId = socialId,
            createdAt = registeredAt
        )

        return socialAccountRepository.save(socialAccount)
    }
}
