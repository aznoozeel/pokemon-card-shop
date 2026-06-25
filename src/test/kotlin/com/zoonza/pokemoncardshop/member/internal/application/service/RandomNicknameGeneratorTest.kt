package com.zoonza.pokemoncardshop.member.internal.application.service

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RandomNicknameGeneratorTest {

    @Test
    fun `랜덤 닉네임은 유효한 닉네임으로 생성된다`() {
        val nicknameGenerator = RandomNicknameGenerator()

        repeat(100) {
            val nickname = nicknameGenerator.generate()

            (nickname.value.length in 3..12) shouldBe true
            nickname.value.any { it.isWhitespace() }.shouldBeFalse()
        }
    }
}
