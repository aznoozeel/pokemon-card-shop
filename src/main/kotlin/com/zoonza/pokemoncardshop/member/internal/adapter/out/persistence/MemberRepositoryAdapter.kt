package com.zoonza.pokemoncardshop.member.internal.adapter.out.persistence

import com.zoonza.pokemoncardshop.member.internal.domain.Member
import com.zoonza.pokemoncardshop.member.internal.domain.MemberRepository
import com.zoonza.pokemoncardshop.member.internal.domain.Nickname
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryAdapter(
    private val jpaMemberRepository: JpaMemberRepository
) : MemberRepository {

    override fun existsByNickname(nickname: Nickname): Boolean {
        return jpaMemberRepository.existsByNickname(nickname)
    }

    override fun save(member: Member): Member {
        return jpaMemberRepository.save(member)
    }
}