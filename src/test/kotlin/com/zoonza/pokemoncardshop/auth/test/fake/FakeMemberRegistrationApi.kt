package com.zoonza.pokemoncardshop.auth.test.fake

import com.zoonza.pokemoncardshop.member.api.MemberRegistrationApi
import com.zoonza.pokemoncardshop.member.api.dto.command.RegisterMemberCommand
import com.zoonza.pokemoncardshop.member.api.dto.result.RegisterMemberResult

class FakeMemberRegistrationApi : MemberRegistrationApi {
    private var nextMemberId = 1L

    val registeredCommands = mutableListOf<RegisterMemberCommand>()

    override fun register(command: RegisterMemberCommand): RegisterMemberResult {
        registeredCommands.add(command)

        return RegisterMemberResult(nextMemberId++)
    }
}
