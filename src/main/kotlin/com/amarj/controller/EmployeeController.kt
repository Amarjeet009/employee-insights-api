package com.amarj.controller

import com.amarj.entity.Employee
import com.amarj.entity.EmployeeAddress
import com.amarj.entity.EmployeeBankingDetail
import com.amarj.entity.EmployeePersonalDetail
import com.amarj.model.EmpAddressRequestDTO
import com.amarj.model.EmpBankingDetailRequestDTO
import com.amarj.model.EmpDetailsRequestDTO
import com.amarj.model.EmpPersonalDetailRequestDTO
import com.amarj.model.EmpRequestDTO
import com.amarj.response.ApiResponse
import com.amarj.response.ResponseBuilder
import com.amarj.service.EmployeeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/api/v1/employee")
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @PostMapping("/saveEmployees")
    fun saveEmployees(@Valid @RequestBody requests: List<EmpRequestDTO>): Mono<ResponseEntity<ApiResponse<List<Employee?>?>>> =
        employeeService.saveEmployees(requests)
            .collectList()
            .map { savedList ->
                ResponseBuilder.success<List<Employee?>?>("${savedList.size} employees created successfully", savedList)
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<List<Employee?>?>(
                        "Failed to create employees: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }


    @PutMapping("/updateEmployee/{id}")
    fun updateEmployee(
        @PathVariable id: Long,
        @Valid @RequestBody request: EmpRequestDTO
    ): Mono<ResponseEntity<ApiResponse<Employee?>>> =
        employeeService.updateEmployee(id, request)
            .map { updatedEmployee ->
                ResponseBuilder.success<Employee?>("Employee updated successfully", updatedEmployee)
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<Employee?>(
                        "Failed to update employee: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }













    @DeleteMapping("/deleteEmployee/{id}")
    fun deleteEmployeeById(@PathVariable id: Long): Mono<ResponseEntity<ApiResponse<Void?>>> {
        return employeeService.deleteEmployeeById(id)
            .then(Mono.just(ResponseBuilder.success<Void?>("Employee deleted successfully", null)))
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<Void?>(
                        "Failed to delete employee: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }


    @PostMapping("/saveEmployeeAddress")
    fun saveEmployeeAddress(
        @Valid @RequestBody request: EmpAddressRequestDTO
    ): Mono<ResponseEntity<ApiResponse<EmployeeAddress?>>> {
        return employeeService.saveEmployeeAddress(request)
            .map { savedAddress ->
                ResponseBuilder.success<EmployeeAddress?>("Employee address saved successfully", savedAddress)
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<EmployeeAddress?>(
                        "Failed to save employee address: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }

    @PutMapping("/updateEmployeeAddress/{employeeId}")
    fun updateEmployeeAddress(
        @PathVariable employeeId: Long,
        @Valid @RequestBody request: EmpAddressRequestDTO
    ): Mono<ResponseEntity<ApiResponse<EmployeeAddress?>>> {
        return employeeService.updateEmployeeAddress(employeeId, request)
            .map { updatedAddress ->
                ResponseBuilder.success<EmployeeAddress?>("Employee address updated successfully", updatedAddress)
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<EmployeeAddress?>(
                        "Failed to update employee address: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }

    @PostMapping("/saveEmployeePersonalDetail")
    fun saveEmployeePersonalDetail(
        @Valid @RequestBody request: EmpPersonalDetailRequestDTO
    ): Mono<ResponseEntity<ApiResponse<EmployeePersonalDetail?>>> {
        return employeeService.saveEmployeePersonalDetail(request)
            .map { savedEmployee ->
                ResponseBuilder.success<EmployeePersonalDetail?>("Employee personal details saved successfully", savedEmployee)
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<EmployeePersonalDetail?>(
                        "Failed to save employee personal details: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }

    @PutMapping("/updateEmployeePersonalDetail/{employeeId}")
    fun updateEmployeePersonalDetail(
        @PathVariable employeeId: Long,
        @Valid @RequestBody request: EmpPersonalDetailRequestDTO
    ): Mono<ResponseEntity<ApiResponse<EmployeePersonalDetail?>>> {
        return employeeService.updateEmployeePersonalDetail(employeeId, request)
            .map { updatedEmployee ->
                ResponseBuilder.success<EmployeePersonalDetail?>("Employee personal details updated successfully", updatedEmployee)
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<EmployeePersonalDetail?>(
                        "Failed to update employee personal details: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }

    @PostMapping("/saveEmployeeBankingDetail")
    fun saveEmployeeBankingDetail(
        @Valid @RequestBody request: EmpBankingDetailRequestDTO
    ): Mono<ResponseEntity<ApiResponse<EmployeeBankingDetail?>>> {
        return employeeService.saveEmployeeBankingDetail(request)
            .map { savedBankingDetail ->
                ResponseBuilder.success<EmployeeBankingDetail?>("Employee banking details saved successfully", savedBankingDetail)
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<EmployeeBankingDetail?>(
                        "Failed to save employee banking details: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }

    @PutMapping("/updateEmployeeBankingDetail/{employeeId}")
    fun updateEmployeeBankingDetail(
        @PathVariable employeeId: Long,
        @Valid @RequestBody request: EmpBankingDetailRequestDTO
    ): Mono<ResponseEntity<ApiResponse<EmployeeBankingDetail?>>> {
        return employeeService.updateEmployeeBankingDetail(employeeId, request)
            .map { updatedBankingDetail ->
                ResponseBuilder.success<EmployeeBankingDetail?>("Employee banking details updated successfully", updatedBankingDetail)
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<EmployeeBankingDetail?>(
                        "Failed to update employee banking details: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }
}