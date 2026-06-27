package com.zoonza.pokemoncardshop.auth.internal.application.dto.command

data class ReissueTokenCommand(
    val refreshToken: String,
)
