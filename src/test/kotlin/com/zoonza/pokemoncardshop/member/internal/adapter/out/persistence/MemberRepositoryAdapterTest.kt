package com.zoonza.pokemoncardshop.member.internal.adapter.out.persistence

import com.zoonza.pokemoncardshop.TestcontainersConfiguration
import com.zoonza.pokemoncardshop.member.internal.domain.Member
import com.zoonza.pokemoncardshop.member.internal.domain.MemberRepository
import com.zoonza.pokemoncardshop.member.internal.domain.Nickname
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.longs.shouldBeGreaterThan
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
    TestcontainersConfiguration::class,
    MemberRepositoryAdapter::class,
)
class MemberRepositoryAdapterTest {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Test
    fun `회원을 저장하면 ID가 할당된다`() {
        val member = Member.register(
            nickname = Nickname("행복한고래1"),
            createdAt = LocalDateTime.of(2026, 6, 25, 10, 30),
        )

        val savedMember = memberRepository.save(member)

        savedMember.id.shouldBeGreaterThan(0L)
    }

    @Test
    fun `저장된 닉네임은 존재한다`() {
        val nickname = Nickname("용감한상어2")
        val member = Member.register(
            nickname = nickname,
            createdAt = LocalDateTime.of(2026, 6, 25, 10, 30),
        )
        memberRepository.save(member)

        memberRepository.existsByNickname(nickname).shouldBeTrue()
    }

    @Test
    fun `저장되지 않은 닉네임은 존재하지 않는다`() {
        val nickname = Nickname("명랑한사자3")

        memberRepository.existsByNickname(nickname).shouldBeFalse()
    }
}
