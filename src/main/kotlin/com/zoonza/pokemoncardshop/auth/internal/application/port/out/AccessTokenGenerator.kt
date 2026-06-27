package com.zoonza.pokemoncardshop.auth.internal.application.port.out

interface AccessTokenGenerator {
    fun generateAccessToken(memberId: Long): String
}
