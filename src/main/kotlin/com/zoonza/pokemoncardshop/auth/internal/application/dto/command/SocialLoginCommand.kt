package com.zoonza.pokemoncardshop.auth.internal.application.dto.command

import com.zoonza.pokemoncardshop.auth.internal.domain.OAuth2Provider

data class SocialLoginCommand(
    val provider: OAuth2Provider,
    val socialId: String
)