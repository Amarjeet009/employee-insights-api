package com.amarj.entity.info

data class DepartmentAnalytics(
    val department: String,
    val highestSalary: Double?,   // nullable to avoid mapping errors
    val averageSalary: Double?,
    val employeeCount: Int?
)