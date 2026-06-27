package com.zoonza.pokemoncardshop.auth.test.fake

import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.IssueTokenCommand
import com.zoonza.pokemoncardshop.auth.internal.application.dto.result.TokenResult
import com.zoonza.pokemoncardshop.auth.internal.application.port.`in`.IssueTokenUseCase

class FakeIssueTokenUseCase : IssueTokenUseCase {
    val issuedCommands = mutableListOf<IssueTokenCommand>()

    override fun issue(command: IssueTokenCommand): TokenResult {
        issuedCommands.add(command)

        return TokenResult(
            memberId = command.memberId,
            accessToken = "access-token",
            refreshToken = "refresh-token",
        )
    }
}
