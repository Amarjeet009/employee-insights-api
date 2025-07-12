package com.amarj.model

import jakarta.validation.constraints.NotBlank

data class DepartmentRequest(
   @field:NotBlank(message = "Department name must not be blank")
   val name: String,
   val status: Int = 1
)
