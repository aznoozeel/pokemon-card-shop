package com.zoonza.pokemoncardshop.auth.test.fake

import com.zoonza.pokemoncardshop.auth.internal.application.port.out.RefreshTokenStore
import java.time.Duration

class FakeRefreshTokenStore : RefreshTokenStore {
    val savedRefreshTokens = mutableMapOf<String, Long>()
    val deletedRefreshTokens = mutableListOf<String>()

    override fun save(refreshToken: String, memberId: Long, ttl: Duration) {
        savedRefreshTokens[refreshToken] = memberId
    }

    override fun findMemberIdByRefreshToken(refreshToken: String): Long? {
        return savedRefreshTokens[refreshToken]
    }

    override fun delete(refreshToken: String) {
        deletedRefreshTokens.add(refreshToken)
        savedRefreshTokens.remove(refreshToken)
    }
}
