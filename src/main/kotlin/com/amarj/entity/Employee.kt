package com.amarj.entity

import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.Date

@Table(name = "employee")
data class Employee(
    @Id
    val id: Long? = null,

    @NotNull(message = "First Name cannot be null")
    @Column("first_name")
    val firstName: String,

    @NotNull(message = "Last Name cannot be null")
    @Column("last_name")
    val lastName: String,

    @Column("middle_name")
    val middleName: String? = null,

    @Column("gender")
    val gender: String? = null,

    @NotNull(message = "Email ID cannot be null")
    @Column("email_id")
    val emailId: String,

    @NotNull(message = "Employee code cannot be null")
    @Column("emp_code")
    val empCode: String,

    @NotNull(message = "Role  cannot be null")
    @Column("role_id")
    val roleId: Long,

    @NotNull(message = "Joined cannot be null")
    @Column("joined_on")
    val joinedOn: Date,

    @NotNull(message = "Added Date cannot be null")
    @Column("created_at")
    val createdAt: Date,

    @Column("updated_at")
    val updatedAt: Date? = null,

    @Column("is_active")
    val status: Int = 1 // 1 for active, 0 for inactive
)
