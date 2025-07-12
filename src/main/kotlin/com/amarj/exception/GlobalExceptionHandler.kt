package com.amarj.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleValidationErrors(ex: WebExchangeBindException): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult
            .fieldErrors
            .map {
                mapOf(
                    "field" to it.field,
                    "message" to (it.defaultMessage ?: "Invalid value"),
                    "rejectedValue" to it.rejectedValue
                )
            }

        val responseBody = mapOf(
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Validation Failed",
            "details" to errors,
            "timestamp" to System.currentTimeMillis()
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody)
    }
}

