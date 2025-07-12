package com.amarj.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
data class NotFoundException (val msg:String) :RuntimeException(msg)

@ResponseStatus(HttpStatus.BAD_REQUEST)
data class BadRequestException (val msg:String) :RuntimeException(msg)


data class ValidationError(
    val field: String,
    val message: String,
    val rejectedValue: Any?
)

data class ValidationErrorResponse(
    val status: Int,
    val error: String,
    val details: List<ValidationError>,
    val timestamp: Long
)
