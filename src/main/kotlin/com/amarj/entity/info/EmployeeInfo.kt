package com.amarj.entity.info

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field


@Document(collection ="emp_info")
data class EmployeeInfo(
    @Id
    val id: String? = null,

    @Field("first_name")
    @field:NotBlank(message = "First Name must not be blank")
    val firstName: String,

    @Field("last_name")
    @field:NotBlank(message = "last Name must not be blank")
    val lastName: String,

    @Field("email")
    @field:Email(message = "invalid email format")
    val email: String,

    @Field("phone")
    val phone: String,

    @Field("gender")
    val gender: String,

    @Field("age")
    @field:Min(18, message = "age must be at least 18")
    @field:Max(65, message = "age must not exceed 65")
    val age: Int?,

    @Field("job_title")
    val jobTitle: String,

    @Field("years_of_experience")
    val yearsOfExperience: Int?,

    @Field("salary")
    val salary: Double?,

    @Field("department")
    val department: String


)
