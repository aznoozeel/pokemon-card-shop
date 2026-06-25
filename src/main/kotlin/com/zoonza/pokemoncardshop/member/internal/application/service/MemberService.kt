package com.zoonza.pokemoncardshop.member.internal.application.service

import com.zoonza.pokemoncardshop.common.error.DomainException
import com.zoonza.pokemoncardshop.member.api.MemberRegistrationApi
import com.zoonza.pokemoncardshop.member.api.dto.command.RegisterMemberCommand
import com.zoonza.pokemoncardshop.member.api.dto.result.RegisterMemberResult
import com.zoonza.pokemoncardshop.member.internal.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val nicknameGenerator: NicknameGenerator,
) : MemberRegistrationApi {

    @Transactional
    override fun register(command: RegisterMemberCommand): RegisterMemberResult {
        val member = Member.register(
            nickname = generateUniqueNickname(),
            createdAt = command.registeredAt,
        )

        val savedMember = memberRepository.save(member)

        return RegisterMemberResult(savedMember.id)
    }

    private fun generateUniqueNickname(): Nickname {
        repeat(MAX_NICKNAME_GENERATION_ATTEMPTS) {
            val nickname = nicknameGenerator.generate()

            if (!memberRepository.existsByNickname(nickname)) {
                return nickname
            }
        }

        throw DomainException(MemberErrorCode.NICKNAME_GENERATION_FAILED)
    }

    companion object {
        private const val MAX_NICKNAME_GENERATION_ATTEMPTS = 10
    }
}
