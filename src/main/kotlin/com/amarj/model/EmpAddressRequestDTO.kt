package com.amarj.model


import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class EmpAddressRequestDTO(

    @field:NotNull(message = "Employee ID cannot be null")
    val employeeId: Long,
    @field:NotNull(message = "Country code cannot be null")
     val countryCode: String,
    @field:NotNull(message = "Phone number cannot be null")
     val phoneNumber: String,

     val alternateNumber: String? = null,

    @field:NotNull(message = "Emergency contact number cannot be null")
     val emergencyContactName: String? = null,

    @field:NotNull(message = "Emergency contact relation cannot be null")
     val emergencyContactRelation: String? = null,

    @field:NotNull(message = "Emergency contact number cannot be null")
     val emergencyContactNumber: String,

    @field:NotNull(message = "House Number cannot be null")
     val houseNumber: String? = null,

    @field:NotNull(message = "Street cannot be null")
     val street: String,

    @field:NotNull(message = "City cannot be null")
     val city: String,

    @field:NotNull(message = "State cannot be null")
     val state: String,

    @field:NotNull(message = "Country cannot be null")
     val country: String,

    @field:NotNull(message = "Postal/ZIP code cannot be null")
     val postalCode: String,

     val createdAt: LocalDate? = null,

     val updatedAt: LocalDate? = null,

     val status: Int? = 1,
)
