package com.amarj.entity

import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("employee_banking_detail")
data class EmployeeBankingDetail(
    @Id
    val id: Int? = null,

    @NotNull(message = "Employee ID cannot be null")
    @Column("emp_id")
    val empId: Int,

    @NotNull(message = "Aadhaar Number cannot be null")
    @Column("adhaar_number")
    val aadhaarNumber: String? = null,

    @NotNull(message = "PAN Number cannot be null")
    @Column("pan_number")
    val panNumber: String?,

    @Column("uan_number")
    val uanNumber: String?,

    @Column("pf_number")
    val pfNumber: String?,

    @Column("work_days")
    val workDays: Int? = 22, // Default to 22 working days in a month

    @NotNull(message = "Bank Name cannot be null")
    @Column("bank_name")
    val bankName: String?,

    @NotNull(message = "Account Number cannot be null")
    @Column("account_number")
    val accountNumber: String?,

    @NotNull(message = "IFSC cannot be null")
    @Column("ifsc_code")
    val ifscCode: String?,

    @NotNull(message = "Branch cannot be null")
    @Column("branch")
    val branch: String?,

    @Column("is_active")
    val status: Int? = 1
)
