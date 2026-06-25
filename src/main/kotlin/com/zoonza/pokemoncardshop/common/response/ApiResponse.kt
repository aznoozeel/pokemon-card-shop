package com.zoonza.pokemoncardshop.common.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ErrorResponse? = null,
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
            )
        }

        fun success(): ApiResponse<Unit> {
            return ApiResponse(
                success = true,
            )
        }

        fun failure(error: ErrorResponse): ApiResponse<Nothing> {
            return ApiResponse(
                success = false,
                error = error,
            )
        }
    }
}
