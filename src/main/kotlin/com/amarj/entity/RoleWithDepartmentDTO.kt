package com.amarj.entity

import java.lang.Long

data class RoleWithDepartmentDTO(
    val id: Long,
    val roleName: String,
    val grade: String,
    val salaryRange: String,
    val departmentId: Long,
    val departmentName: String,
    val isActive: Integer
)