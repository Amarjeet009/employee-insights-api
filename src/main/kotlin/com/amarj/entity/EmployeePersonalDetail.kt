package com.amarj.entity

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.Date

@Table(name = "employee_personal_detail")
data class EmployeePersonalDetail(

    @Id
    val id: Long? = null,

    @NotNull(message = "Employee ID cannot be null")
    @Column("emp_id")
    val employeeId: Long,

    @NotNull(message = "Emp DOB cannot be null")
    @Column("emp_date_of_birth")
    val empDOB: Date? = null,

    @NotNull(message = "Personal email ID cannot be null")
    @Column("personal_email_id")
    val personalEmailId: String? = null,

    @NotNull(message = "Father name cannot be null")
    @Column("father_name")
    val fatherName: String? = null,

    @NotNull(message = "Father DOB cannot be null")
    @Column("father_dob")
    val fatherDOB: Date? = null,

    @Column("father_occupation")
    val fatherOccupation: String? = null,

    @NotNull(message = "Mother name cannot be null")
    @Column("mother_name")
    val motherName: String? = null,

    @Column("mother_dob")
    val motherDOB: Date? = null,

    @Column("mother_occupation")
    val motherOccupation: String? = null,

    @NotNull(message = "Specify marital status")
    @Column("is_married")
    val isMarried: Int?= 0, // 1 for married, 0 for unmarried


    @Column("spouse_name")
    val spouseName: String? = null,

    @Column("spouse_dob")
    val spouseDOB: Date? = null,

    @Column("spouse_occupation")
    val spouseOccupation: String? = null,

    @Column("children_count")
    @Max(3, message = "Maximum 3 children allowed")
    val childrenCount: Int? = 0, // Number of children

    @Column("child_first_name")
    val childFirstName: String? = null, // first child name

    @Column("child_first_dob")
    val childFirstDOB: Date? = null,

    @Column("child_second_name")
    val childSecondName: String? = null, // second child name

    @Column("child_second_dob")
    val childSecondDOB: Date? = null,

    @Column("child_third_name")
    val childThirdName: String? = null, // third child name

    @Column("child_third_dob")
    val childThirdDOB: Date? = null,

    @Column("updated_at")
    val updatedAt: Date? = null,

    @Column("is_active")
    val status: Int? = 1, // 1 for active, 0 for inactive

)
