package com.zoonza.pokemoncardshop.auth.internal.application.port.`in`

import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.IssueTokenCommand
import com.zoonza.pokemoncardshop.auth.internal.application.dto.result.TokenResult

interface IssueTokenUseCase {
    fun issue(command: IssueTokenCommand): TokenResult
}
