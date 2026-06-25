package com.zoonza.pokemoncardshop.global.exception

import com.zoonza.pokemoncardshop.common.error.DomainException
import com.zoonza.pokemoncardshop.common.error.ErrorStatus
import com.zoonza.pokemoncardshop.common.response.ApiResponse
import com.zoonza.pokemoncardshop.common.response.ErrorResponse
import com.zoonza.pokemoncardshop.common.response.ValidationError
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(exception: DomainException): ResponseEntity<ApiResponse<Nothing>> {
        val errorCode = exception.errorCode

        return ResponseEntity
            .status(errorCode.status.toHttpStatus())
            .body(ApiResponse.failure(
                    ErrorResponse(
                        code = errorCode.code,
                        message = errorCode.message,
                    )
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
    ): ResponseEntity<ApiResponse<Nothing>> {
        val message = exception.bindingResult.fieldErrors.firstOrNull()?.defaultMessage
            ?: "요청 값이 올바르지 않습니다."

        val validationErrors = exception.bindingResult.fieldErrors.map {
            ValidationError(
                field = it.field,
                message = it.defaultMessage ?: "요청 값이 올바르지 않습니다.",
            )
        }

        return badRequest(
            message = message,
            validationErrors = validationErrors,
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        exception: ConstraintViolationException,
    ): ResponseEntity<ApiResponse<Nothing>> {
        val message = exception.constraintViolations.firstOrNull()?.message
            ?: "요청 값이 올바르지 않습니다."

        val validationErrors = exception.constraintViolations.map {
            ValidationError(
                field = it.propertyPath.toString(),
                message = it.message,
            )
        }

        return badRequest(
            message = message,
            validationErrors = validationErrors,
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        return badRequest(exception.message ?: "요청 값이 올바르지 않습니다.")
    }

    private fun badRequest(
        message: String,
        validationErrors: List<ValidationError>? = null,
    ): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(
                    ErrorResponse(
                        code = "INVALID_REQUEST",
                        message = message,
                        validationErrors = validationErrors,
                    )
                )
            )
    }

    private fun ErrorStatus.toHttpStatus(): HttpStatus {
        return when (this) {
            ErrorStatus.BAD_REQUEST -> HttpStatus.BAD_REQUEST
            ErrorStatus.UNAUTHORIZED -> HttpStatus.UNAUTHORIZED
            ErrorStatus.FORBIDDEN -> HttpStatus.FORBIDDEN
            ErrorStatus.NOT_FOUND -> HttpStatus.NOT_FOUND
            ErrorStatus.CONFLICT -> HttpStatus.CONFLICT
            ErrorStatus.INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}
