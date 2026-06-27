package com.zoonza.pokemoncardshop.auth.internal.adapter.`in`.security.oauth2

import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.SocialLoginCommand
import com.zoonza.pokemoncardshop.auth.internal.application.port.`in`.SocialLoginUseCase
import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CustomOidcUserService(
    private val socialLoginUseCase: SocialLoginUseCase,
) : OidcUserService() {

    @Transactional
    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        val oidcUser = super.loadUser(userRequest)

        val provider = OAuth2Provider.from(userRequest.clientRegistration.registrationId)

        val userInfo = OAuth2UserInfoMapper.map(
            provider = provider,
            attributes = oidcUser.attributes,
        )

        val result = socialLoginUseCase.login(
            SocialLoginCommand(
                provider = provider,
                socialId = userInfo.socialId,
            ),
        )

        return CustomOidcUser(
            memberId = result.memberId,
            delegate = oidcUser,
        )
    }
}
