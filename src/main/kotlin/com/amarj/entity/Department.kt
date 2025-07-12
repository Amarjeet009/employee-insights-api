package com.amarj.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table



@Table(name = "department")
data class Department(
    @Id
    val id: Long ? = null,

    @Column("department_name")
    val name: String,

    @Column("is_active")
    val status: Int
)
