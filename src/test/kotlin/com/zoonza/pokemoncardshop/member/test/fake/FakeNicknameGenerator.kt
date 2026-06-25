package com.zoonza.pokemoncardshop.member.test.fake

import com.zoonza.pokemoncardshop.member.internal.domain.Nickname
import com.zoonza.pokemoncardshop.member.internal.application.service.NicknameGenerator

class FakeNicknameGenerator : NicknameGenerator {
    private val nicknames = ArrayDeque<Nickname>()
    private var index = 0

    fun addNicknames(nicknames: Iterable<Nickname>) {
        this.nicknames.addAll(nicknames)
    }

    fun addNicknames(vararg nicknames: Nickname) {
        this.nicknames.addAll(nicknames)
    }

    override fun generate(): Nickname {
        return nicknames[index++]
    }
}
