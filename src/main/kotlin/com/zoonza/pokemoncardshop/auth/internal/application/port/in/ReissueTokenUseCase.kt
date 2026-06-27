package com.zoonza.pokemoncardshop.auth.internal.application.port.`in`

import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.ReissueTokenCommand
import com.zoonza.pokemoncardshop.auth.internal.application.dto.result.TokenResult

interface ReissueTokenUseCase {
    fun reissue(command: ReissueTokenCommand): TokenResult
}
