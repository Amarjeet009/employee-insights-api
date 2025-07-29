package com.amarj.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotNull
import java.time.LocalDate


data class EmployeeRequestDTO(
    @field:NotNull(message = "First Name cannot be null")
    val firstName: String,

    @field:NotNull(message = "Last Name cannot be null")
    val lastName: String,


    val middleName: String? = null,

    @field:NotNull(message="Gender cannot be null")
    val gender: String? = null,

    val emailId: String,

    val empCode: String,

    @field:NotNull(message = "Role ID cannot be null")
    val roleId: Long,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val joinedOn: LocalDate? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val createdAt: LocalDate? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val updatedAt: LocalDate? = null,

    val status: Int = 1 // 1 for active, 0 for inactive
)
