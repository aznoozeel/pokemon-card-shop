package com.zoonza.pokemoncardshop.auth.internal.adapter.out.persistence

import com.zoonza.pokemoncardshop.auth.internal.application.port.out.RefreshTokenStore
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisRefreshTokenStore(
    private val redisTemplate: StringRedisTemplate,
) : RefreshTokenStore {

    override fun save(refreshToken: String, memberId: Long, ttl: Duration) {
        redisTemplate.opsForValue()
            .set(key(refreshToken), memberId.toString(), ttl)
    }

    override fun findMemberIdByRefreshToken(refreshToken: String): Long? {
        return redisTemplate.opsForValue()
            .get(key(refreshToken))
            ?.toLongOrNull()
    }

    override fun delete(refreshToken: String) {
        redisTemplate.delete(key(refreshToken))
    }

    private fun key(refreshToken: String): String {
        return "$KEY_PREFIX$refreshToken"
    }

    companion object {
        private const val KEY_PREFIX = "refresh:"
    }
}
