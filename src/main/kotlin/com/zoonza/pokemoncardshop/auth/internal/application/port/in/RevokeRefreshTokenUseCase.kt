package com.zoonza.pokemoncardshop.auth.internal.application.port.`in`

import com.zoonza.pokemoncardshop.auth.internal.application.dto.command.RevokeRefreshTokenCommand

interface RevokeRefreshTokenUseCase {
    fun revoke(command: RevokeRefreshTokenCommand)
}
