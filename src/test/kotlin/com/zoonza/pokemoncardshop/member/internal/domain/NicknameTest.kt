package com.zoonza.pokemoncardshop.member.internal.domain

import com.zoonza.pokemoncardshop.common.error.DomainException
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class NicknameTest {

    @Test
    fun `닉네임은 3자 이상 12자 이하면 생성된다`() {
        Nickname("가나다").value shouldBe "가나다"
        Nickname("가나다라마바사아자차카타").value shouldBe "가나다라마바사아자차카타"
    }

    @Test
    fun `닉네임이 3자 미만이면 예외가 발생한다`() {
        val exception = assertThrows<DomainException> {
            Nickname("가나")
        }

        exception.errorCode shouldBe MemberErrorCode.NICKNAME_LENGTH_OUT_OF_RANGE
    }

    @Test
    fun `닉네임이 12자를 초과하면 예외가 발생한다`() {
        val exception = assertThrows<DomainException> {
            Nickname("가나다라마바사아자차카타파")
        }

        exception.errorCode shouldBe MemberErrorCode.NICKNAME_LENGTH_OUT_OF_RANGE
    }

    @Test
    fun `닉네임에 공백이 포함되면 예외가 발생한다`() {
        val exception = assertThrows<DomainException> {
            Nickname("가나다 라")
        }

        exception.errorCode shouldBe MemberErrorCode.NICKNAME_CONTAINS_WHITESPACE
    }
}
