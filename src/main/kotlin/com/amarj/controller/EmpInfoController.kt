package com.amarj.controller

import com.amarj.entity.Employee
import com.amarj.model.EmpDetailsRequestDTO
import com.amarj.response.ApiResponse
import com.amarj.response.ResponseBuilder
import com.amarj.service.EmpInfoService
import com.amarj.service.EmployeeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RequestMapping("/api/v1/emp/info")
@RestController
class EmpInfoController(
    private val empInfoService: EmpInfoService
) {
    // Define endpoints for employee information management here
    // For example, you can add methods to handle CRUD operations for employee info
    // using empInfoService to interact with the database.
    @GetMapping("/getAllEmployees")
    fun getAllEmployees(): Mono<ResponseEntity<ApiResponse<List<Employee>?>>> {
        return empInfoService.getAllEmployees()
            .collectList()
            .map { empList: List<Employee>? ->
                ResponseBuilder.success<List<Employee>?>("Employees retrieved successfully", empList)
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<List<Employee>?>(
                        "Failed to retrieve employees: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }

    @PostMapping("/findEmployeeDetails")
    fun findEmployeeDetails(@Valid @RequestBody request: EmpDetailsRequestDTO): Mono<ResponseEntity<ApiResponse<List<Employee?>?>>> {
        return empInfoService.findEmployeeDetails(request)
            .collectList()
            .map { employeeList ->
                ResponseBuilder.success<List<Employee?>?>("Employees retrieved successfully", employeeList)
            }
            .onErrorResume { ex ->
                Mono.just(ResponseBuilder.error<List<Employee?>?>("Failed to retrieve employees: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR))
            }
    }
}