package com.zoonza.pokemoncardshop.member.internal.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MemberTest {

    @Test
    fun `회원은 닉네임과 생성일시로 가입된다`() {
        val nickname = Nickname("테스트닉")
        val createdAt = LocalDateTime.of(2026, 6, 25, 10, 30)

        val member = Member.register(
            nickname = nickname,
            createdAt = createdAt
        )

        member.id shouldBe 0L
        member.nickname shouldBe nickname
        member.createdAt shouldBe createdAt
        member.updatedAt shouldBe createdAt
    }
}
