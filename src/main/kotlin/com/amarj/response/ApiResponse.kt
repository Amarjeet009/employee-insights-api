package com.amarj.response

import org.springframework.http.HttpStatus


data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
)


