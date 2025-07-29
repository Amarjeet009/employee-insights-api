package com.amarj.model

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDate

data class EmpPersonalDetailRequestDTO(
    @field:NotNull(message = "Employee ID cannot be null")
    val employeeId: Long,
    @field:NotNull(message = "Employee Date of Birth cannot be null")
    val empDOB: LocalDate? = null,
    @field:NotNull(message = "Personal email ID cannot be null")
    val personalEmailId: String? = null,
    @field:NotNull(message = "Father name cannot be null")
    val fatherName: String? = null,
    @field:NotNull(message = "Father Date of Birth cannot be null")
    val fatherDOB: LocalDate? = null,
    @field:NotNull(message = "Father occupation cannot be null")
    val fatherOccupation: String? = null,
    @field:NotNull(message = "Mother name cannot be null")
    val motherName: String? = null,
    @field:NotNull(message = "Mother Date of Birth cannot be null")
    val motherDOB: LocalDate? = null,
    @field:NotNull(message = "Mother occupation cannot be null")
    val motherOccupation: String? = null,
    @field:NotNull(message = "Specify marital status")

    val isMarried: Int?= 0, // 1 for married, 0 for unmarried

    val marriageDate: LocalDate? = null, // Date of marriage, if married

    val spouseName: String? = null,

    val spouseDOB: LocalDate? = null,

    val spouseOccupation: String? = null,

    val childrenCount: Int? = 0, // Number of children

    val childFirstName: String? = null, // first child name

    val childFirstDOB: LocalDate? = null,

    val childSecondName: String? = null, // second child name

    val childSecondDOB: LocalDate? = null,

    val childThirdName: String? = null, // third child name

    val childThirdDOB: LocalDate? = null,

    val updatedAt: LocalDate? = null,

    val status: Int? = 1, // 1 for active, 0 for inactive

)
