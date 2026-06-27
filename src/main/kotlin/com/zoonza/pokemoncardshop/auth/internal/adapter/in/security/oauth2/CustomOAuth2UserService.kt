package com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.security.oauth2

import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.SocialLoginCommand
import com.zoonza.pokemoncardshop.auth.internal.application.port.`in`.SocialLoginUseCase
import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CustomOAuth2UserService(
    private val socialLoginUseCase: SocialLoginUseCase
) : DefaultOAuth2UserService() {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        val provider = OAuth2Provider.from(userRequest.clientRegistration.registrationId)

        val userInfo = OAuth2UserInfoMapper.map(
            provider = provider,
            attributes = oAuth2User.attributes
        )

        val result = socialLoginUseCase.login(
            SocialLoginCommand(
                provider = provider,
                socialId = userInfo.socialId
            )
        )

        return CustomOAuth2User(
            memberId = result.memberId,
            delegate = oAuth2User,
        )
    }
}
