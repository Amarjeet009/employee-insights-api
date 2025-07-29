package com.amarj.entity

import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "employee_address")
data class EmployeeAddress(
    @Id
    private val id: Long? = null,
    
    @Column("emp_id")
    private val employeeId: Long,

    @NotNull(message = "Country code cannot be null")
    @Column("country_code")
    private val countryCode: String,

    @NotNull(message = "Phone number cannot be null")
    @Column("phone_number")
    private val phoneNumber: String,

    @Column("alternate_number")
    private val alternateNumber: String? = null,

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
    private val city: String,

    @NotNull(message = "State cannot be null")
    @Column("state")
    private val state: String,

    @NotNull(message = "Country cannot be null")
    @Column("country")
    private val country: String,

    @NotNull(message = "Postal/ZIP code cannot be null")
    @Column("postal_code")
    private val postalCode: String,

    @Column("updated_at")
    private val updatedAt: LocalDate? = null,

    @Column("is_active")
    private val status: Int? = 1,
)
