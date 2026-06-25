package com.zoonza.pokemoncardshop.member.api

import com.zoonza.pokemoncardshop.member.api.dto.command.RegisterMemberCommand
import com.zoonza.pokemoncardshop.member.api.dto.result.RegisterMemberResult

interface MemberRegistrationApi {
    fun register(command: RegisterMemberCommand): RegisterMemberResult
}