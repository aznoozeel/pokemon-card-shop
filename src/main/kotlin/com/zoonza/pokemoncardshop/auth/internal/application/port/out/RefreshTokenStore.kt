package com.zoonza.pokemoncardshop.auth.internal.application.port.out

import java.time.Duration

interface RefreshTokenStore {
    fun save(refreshToken: String, memberId: Long, ttl: Duration)

    fun findMemberIdByRefreshToken(refreshToken: String): Long?

    fun delete(refreshToken: String)
}
