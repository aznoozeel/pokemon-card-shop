package com.zoonza.pokemoncardshop.common.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val code: String,
    val message: String,
    val validationErrors: List<ValidationError>? = null,
)

data class ValidationError(
    val field: String,
    val message: String,
)
