package com.zoonza.pokemoncardshop.member.test.fake

import com.zoonza.pokemoncardshop.member.internal.domain.Member
import com.zoonza.pokemoncardshop.member.internal.domain.MemberRepository
import com.zoonza.pokemoncardshop.member.internal.domain.Nickname

class FakeMemberRepository : MemberRepository {
    private var nextId = 1L
    private val duplicatedNicknames = mutableSetOf<Nickname>()

    val savedMembers = mutableListOf<Member>()
    val checkedNicknames = mutableListOf<Nickname>()

    fun addDuplicatedNicknames(nicknames: Iterable<Nickname>) {
        duplicatedNicknames.addAll(nicknames)
    }

    fun addDuplicatedNicknames(vararg nicknames: Nickname) {
        duplicatedNicknames.addAll(nicknames)
    }

    override fun existsByNickname(nickname: Nickname): Boolean {
        checkedNicknames.add(nickname)
        return nickname in duplicatedNicknames
    }

    override fun save(member: Member): Member {
        val savedMember = Member.register(
            nickname = member.nickname,
            createdAt = member.createdAt,
        )

        val idField = savedMember::class.java.getDeclaredField("id")
        idField.isAccessible = true
        idField.set(savedMember, nextId++)

        savedMembers.add(savedMember)

        return savedMember
    }
}
