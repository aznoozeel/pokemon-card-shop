package com.zoonza.pokemoncardshop.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val oAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
    private val oAuth2SuccessHandler: AuthenticationSuccessHandler,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll()
            }
            .oauth2Login { oauth2 ->
                oauth2.userInfoEndpoint { userInfo ->
                    userInfo.userService(oAuth2UserService)
                }
                oauth2.successHandler(oAuth2SuccessHandler)
            }
            .oauth2ResourceServer { resourceServer ->
                resourceServer.jwt {  }
            }
            .build()
    }
}
