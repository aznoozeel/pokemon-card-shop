package com.zoonza.pokemoncardshop.member.internal.domain

import com.zoonza.pokemoncardshop.common.error.DomainException
import jakarta.persistence.Embeddable

@Embeddable
data class Nickname(val value: String) {
    init {
        if (value.length !in MIN_LENGTH..MAX_LENGTH) {
            throw DomainException(MemberErrorCode.NICKNAME_LENGTH_OUT_OF_RANGE)
        }

        if (value.any { it.isWhitespace() }) {
            throw DomainException(MemberErrorCode.NICKNAME_CONTAINS_WHITESPACE)
        }
    }

    companion object {
        private const val MIN_LENGTH = 3
        private const val MAX_LENGTH = 12
    }
}