package com.amarj.entity

import org.springframework.data.annotation.Id
import java.time.LocalDate

data class EmployeeDetailsDto(
    val id: Long,
    val empCode: String,
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val emailId: String,
    val gender: String,
    val joinedOn: LocalDate,
    val isActive: Integer,
    val grade: String,
    val roleName: String,
    val salaryRange: String,
    val departmentName: String
)