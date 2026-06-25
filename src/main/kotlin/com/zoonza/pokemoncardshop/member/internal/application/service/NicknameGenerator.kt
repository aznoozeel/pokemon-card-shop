package com.zoonza.pokemoncardshop.member.internal.application.service

import com.zoonza.pokemoncardshop.member.internal.domain.Nickname

interface NicknameGenerator {
    fun generate(): Nickname
}
