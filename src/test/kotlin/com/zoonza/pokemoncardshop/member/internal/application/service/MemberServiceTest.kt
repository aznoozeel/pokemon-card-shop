package com.zoonza.pokemoncardshop.member.internal.application.service

import com.zoonza.pokemoncardshop.common.error.DomainException
import com.zoonza.pokemoncardshop.member.api.dto.command.RegisterMemberCommand
import com.zoonza.pokemoncardshop.member.internal.domain.MemberErrorCode
import com.zoonza.pokemoncardshop.member.internal.domain.Nickname
import com.zoonza.pokemoncardshop.member.test.fake.FakeMemberRepository
import com.zoonza.pokemoncardshop.member.test.fake.FakeNicknameGenerator
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class MemberServiceTest {
    private lateinit var memberRepository: FakeMemberRepository
    private lateinit var nicknameGenerator: FakeNicknameGenerator
    private lateinit var memberService: MemberService

    @BeforeEach
    fun setUp() {
        memberRepository = FakeMemberRepository()
        nicknameGenerator = FakeNicknameGenerator()

        memberService = MemberService(
            memberRepository = memberRepository,
            nicknameGenerator = nicknameGenerator,
        )
    }

    @Test
    fun `회원 가입 시 중복되지 않은 닉네임으로 회원을 저장하고 회원 ID를 반환한다`() {
        val nickname = Nickname("행복한고래1")
        nicknameGenerator.addNicknames(nickname)
        val registeredAt = LocalDateTime.of(2026, 6, 25, 10, 30)

        val result = memberService.register(RegisterMemberCommand(registeredAt))

        result.memberId shouldBe 1L
        memberRepository.checkedNicknames.shouldContainExactly(nickname)
        memberRepository.savedMembers.size shouldBe 1
        memberRepository.savedMembers.first().nickname shouldBe nickname
        memberRepository.savedMembers.first().createdAt shouldBe registeredAt
        memberRepository.savedMembers.first().updatedAt shouldBe registeredAt
    }

    @Test
    fun `중복 닉네임이 생성되면 중복되지 않은 닉네임이 나올 때까지 다시 생성한다`() {
        val duplicatedNickname = Nickname("행복한고래1")
        val uniqueNickname = Nickname("용감한상어2")
        memberRepository.addDuplicatedNicknames(duplicatedNickname)
        nicknameGenerator.addNicknames(duplicatedNickname, duplicatedNickname, uniqueNickname)

        val result = memberService.register(
            RegisterMemberCommand(LocalDateTime.of(2026, 6, 25, 10, 30)),
        )

        result.memberId shouldBe 1L
        memberRepository.checkedNicknames.shouldContainExactly(
            duplicatedNickname,
            duplicatedNickname,
            uniqueNickname,
        )
        memberRepository.savedMembers.first().nickname shouldBe uniqueNickname
    }

    @Test
    fun `닉네임 생성이 10회 모두 중복이면 예외가 발생하고 회원을 저장하지 않는다`() {
        val duplicatedNicknames = (1..10).map { Nickname("중복닉$it") }
        memberRepository.addDuplicatedNicknames(duplicatedNicknames)
        nicknameGenerator.addNicknames(duplicatedNicknames)

        val exception = assertThrows<DomainException> {
            memberService.register(
                RegisterMemberCommand(LocalDateTime.of(2026, 6, 25, 10, 30)),
            )
        }

        exception.errorCode shouldBe MemberErrorCode.NICKNAME_GENERATION_FAILED
        memberRepository.checkedNicknames.shouldContainExactly(duplicatedNicknames)
        memberRepository.savedMembers.size shouldBe 0
    }
}
