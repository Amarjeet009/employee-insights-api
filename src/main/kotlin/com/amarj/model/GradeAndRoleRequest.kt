package com.amarj.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Min

data class GradeAndRoleRequest(
    @field:NotNull(message = "Grade must not be null")
    @field:Min(value = 20, message = "Grade must be greater than 0")
    val grade: Long,

    @field:NotBlank(message = "Role name must not be blank")
    val roleName: String,

    @field:NotBlank(message = "Salary range must not be blank")
    val salaryRange: String,

    @field:NotNull(message = "Department ID must not be null")
    @field:Min(value = 1, message = "Department ID must be greater than 0")
    val departmentId: Long,

    val status: Int = 1
)
