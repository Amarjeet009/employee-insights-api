package com.amarj.model

import jakarta.validation.constraints.NotNull

data class EmpBankingDetailRequestDTO(
    val id: Int? = null,

    @field:NotNull(message = "Employee ID cannot be null")
    val empId: Int,

    @field:NotNull(message = "Aadhaar Number cannot be null")
    val aadhaarNumber: String? = null,

    @field:NotNull(message = "PAN Number cannot be null")
    val panNumber: String? = null,

    val uanNumber: String? = null,
    val pfNumber: String? = null,
    val workDays: Int? = 22,

    @field:NotNull(message = "Bank Name cannot be null")
    val bankName: String? = null,

    @field:NotNull(message = "Account Number cannot be null")
    val accountNumber: String? = null,

    @field:NotNull(message = "IFSC Code cannot be null")
    val ifscCode: String? = null,

    @field:NotNull(message = "Branch cannot be null")
    val branch: String? = null,

    val status: Int? = 1
)

