package com.amarj.response

import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus

object ResponseBuilder {

    fun <T> success(message: String, data: T? = null, status: HttpStatus = HttpStatus.OK): ResponseEntity<ApiResponse<T>> =
        ResponseEntity.status(status).body(ApiResponse(status.value(), message, data))

    fun <T> error(message: String, status: HttpStatus = HttpStatus.BAD_REQUEST): ResponseEntity<ApiResponse<T?>> =
        ResponseEntity.status(status).body(ApiResponse(status.value(), message, null))
}


