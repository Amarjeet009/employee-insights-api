package com.amarj.entity

import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.Date

@Table(name = "employee_address")
data class EmployeeAddress(
    @Id
    val id: Long? = null,
    
    @Column("emp_id")
    val employeeId: Long,

    @NotNull(message = "Country code cannot be null")
    @Column("country_code")
    val countryCode: String,

    @NotNull(message = "Phone number cannot be null")
    @Column("phone_number")
    val phoneNumber: String,

    @Column("alternate_number")
    val alternateNumber: String? = null,

    @Column("emergency_contact_name")
    private val emergencyContactName: String? = null,

    @Column("emergency_contact_relation")
    private val emergencyContactRelation: String? = null,

    @NotNull(message = "Emergency contact number cannot be null")
    @Column("emergency_contact_number")
    private val emergencyContactNumber: String,

    @Column("house_number")
    private val houseNumber: String? = null,

    @NotNull(message = "Street cannot be null")
    @Column("street")
    private val street: String,

    @NotNull(message = "City cannot be null")
    @Column("city")
    val city: String,

    @NotNull(message = "State cannot be null")
    @Column("state")
    val state: String,

    @NotNull(message = "Country cannot be null")
    @Column("country")
    val country: String,

    @NotNull(message = "Postal/ZIP code cannot be null")
    @Column("postal_code")
    val postalCode: String,

    @Column("updated_at")
    val updatedAt: Date? = null,

    @Column("is_active")
    val status: Int? = 1,
)
