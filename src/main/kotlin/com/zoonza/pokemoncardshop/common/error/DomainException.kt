package com.zoonza.pokemoncardshop.common.error

class DomainException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message,
    cause: Throwable? = null
) : RuntimeException(message, cause)