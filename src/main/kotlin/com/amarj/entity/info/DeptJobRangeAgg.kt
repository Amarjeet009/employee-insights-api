package com.amarj.entity.info

data class DeptJobRangeAgg(
    val department: String,
    val jobTitle: String,
    val salaryRange: String,
    val expRange: String,
    val ageRange: String,
    val employeeCount: Int,
    val avgSalary: Double,
    val avgExperience: Double
)
