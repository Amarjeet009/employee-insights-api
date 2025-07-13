package com.amarj.entity

import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table



@Table(name = "department")
data class Department(
    @Id
    val id: Long ? = null,

    @NotNull(message = "Department name cannot be null")
    @Column("department_name")
    val name: String,

    @NotNull(message = "Status cannot be null")
    @Column("is_active")
    val status: Int = 1 // 1 for active, 0 for inactive
)
