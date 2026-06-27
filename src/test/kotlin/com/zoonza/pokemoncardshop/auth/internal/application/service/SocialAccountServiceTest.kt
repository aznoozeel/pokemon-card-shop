package com.zoonza.pokemoncardshop.auth.internal.application.service

import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.SocialLoginCommand
import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider
import com.zoonza.pokemoncardshop.auth.internal.domain.SocialAccount
import com.zoonza.pokemoncardshop.auth.test.fake.FakeMemberRegistrationApi
import com.zoonza.pokemoncardshop.auth.test.fake.FakeSocialAccountRepository
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class SocialAccountServiceTest {
    private lateinit var memberRegistrationApi: FakeMemberRegistrationApi
    private lateinit var socialAccountRepository: FakeSocialAccountRepository
    private lateinit var socialAccountService: SocialAccountService

    @BeforeEach
    fun setUp() {
        memberRegistrationApi = FakeMemberRegistrationApi()
        socialAccountRepository = FakeSocialAccountRepository()
        socialAccountService = SocialAccountService(
            memberRegistrationApi = memberRegistrationApi,
            socialAccountRepository = socialAccountRepository,
        )
    }

    @Test
    fun `신규 소셜 로그인 시 회원과 소셜 계정을 등록하고 회원 ID를 반환한다`() {
        val result = socialAccountService.login(
            SocialLoginCommand(
                provider = OAuth2Provider.GOOGLE,
                socialId = "google-social-id",
            )
        )

        result.memberId shouldBe 1L
        memberRegistrationApi.registeredCommands.shouldHaveSize(1)
        socialAccountRepository.savedSocialAccounts.shouldHaveSize(1)

        val savedSocialAccount = socialAccountRepository.savedSocialAccounts.first()
        savedSocialAccount.memberId shouldBe 1L
        savedSocialAccount.provider shouldBe OAuth2Provider.GOOGLE
        savedSocialAccount.socialId shouldBe "google-social-id"
        savedSocialAccount.lastLoginAt shouldNotBe null
    }

    @Test
    fun `기존 소셜 로그인 시 회원을 새로 만들지 않고 마지막 로그인 시간을 갱신한다`() {
        val existingSocialAccount = SocialAccount.register(
            memberId = 3L,
            provider = OAuth2Provider.GOOGLE,
            socialId = "google-social-id",
            createdAt = LocalDateTime.of(2026, 6, 25, 10, 30),
        )
        socialAccountRepository.addSocialAccounts(existingSocialAccount)

        val result = socialAccountService.login(
            SocialLoginCommand(
                provider = OAuth2Provider.GOOGLE,
                socialId = "google-social-id",
            )
        )

        result.memberId shouldBe 3L
        memberRegistrationApi.registeredCommands.shouldHaveSize(0)
        socialAccountRepository.savedSocialAccounts.shouldHaveSize(0)
        existingSocialAccount.lastLoginAt shouldNotBe null
    }
}
