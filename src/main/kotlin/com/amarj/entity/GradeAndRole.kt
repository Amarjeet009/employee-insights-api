package com.amarj.entity

import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "grade_and_role")
data class GradeAndRole(

    @Id
    @Column("id")
    val id: Long? = null,

    @NotNull(message = "Grade cannot be null")
    @Column("grade")
    val grade: Long,

    @NotNull(message = "Role name cannot be null")
    @Column("role_name")
    val roleName: String,

    @NotNull(message = "Salary range cannot be null")
    @Column("salary_range")
    val salaryRange: String,

    @NotNull(message = "Department ID cannot be null")
    @Column("department_id")
    val departmentId: Long,

    @NotNull(message = "Status cannot be null")
    @Column("is_active")
    val status: Int = 1
)
