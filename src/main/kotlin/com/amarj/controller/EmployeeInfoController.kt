package com.amarj.controller

import com.amarj.entity.info.DepartmentAnalytics
import com.amarj.entity.info.DepartmentSalary
import com.amarj.entity.info.DeptJobRangeAgg
import com.amarj.entity.info.EmployeeInfo
import com.amarj.entity.info.analytics.GenderScoreSummary
import com.amarj.entity.info.analytics.RoleScoreSummary
import com.amarj.response.ApiResponse
import com.amarj.response.ResponseBuilder
import com.amarj.service.EmployeeInfoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/emp/info")
class EmployeeInfoController(val empInfoService: EmployeeInfoService) {

    @GetMapping("/getAllEmpInfo")
    fun getAllEmpInfo(): Mono<ResponseEntity<ApiResponse<List<EmployeeInfo>?>>> {
        return empInfoService.getAllEmpInfo()
            .collectList()
            .map { empList ->
                ResponseBuilder.success<List<EmployeeInfo>?>("Employees Info retrieved successfully", empList.toList())
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<List<EmployeeInfo>?>(
                        "Failed to retrieve employees info: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }




    @GetMapping("/getHighestSalaryPerDepartment")
    fun getHighestSalaryPerDepartment(): Mono<ResponseEntity<ApiResponse<List<DepartmentSalary>?>>> {
        return  empInfoService.getHighestSalaryPerDepartment()
            .collectList()
            .map{ dataList ->
                ResponseBuilder.success<List<DepartmentSalary>?>("Data retrieved successfully", dataList.toList())
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<List<DepartmentSalary>?>(
                        "Failed to retrieve department salary: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }


    @GetMapping("/getDepartmentAnalytics")
    fun getDepartmentAnalytics(): Mono<ResponseEntity<ApiResponse<List<DepartmentAnalytics>?>>>{
        return  empInfoService.getDepartmentAnalytics()
            .collectList()
            .map{ dataList ->
                ResponseBuilder.success<List<DepartmentAnalytics>?>("Data retrieved successfully", dataList.toList())
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<List<DepartmentAnalytics>?>(
                        "Failed to retrieve department analytics data: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }



    @PostMapping("/createEmpInfo")
    fun createEmpInfo(@Valid @RequestBody employeeInfo:  List<EmployeeInfo>): Mono<ResponseEntity<ApiResponse<List<EmployeeInfo?>?>>> =
        empInfoService.createEmpInfo(employeeInfo)
            .collectList()
            .map { savedList ->
                ResponseBuilder.success<List<EmployeeInfo?>?>(
                    "${savedList.size} employees created successfully",
                    savedList
                )
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<List<EmployeeInfo?>?>(
                        "Failed to create employees: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }



        @PutMapping("updateEmpInfo/{id}")
        fun updateEmpInfo(
            @PathVariable id: String,
            @Valid @RequestBody employeeInfo: EmployeeInfo
        ): Mono<ResponseEntity<ApiResponse<EmployeeInfo?>>> =
            empInfoService.updateEmpInfo(id, employeeInfo)
                .map { updatedEmpInfo ->
                    ResponseBuilder.success<EmployeeInfo?>("Employee updated successfully", updatedEmpInfo)
                }
                .onErrorResume { ex ->
                    Mono.just(
                        ResponseBuilder.error<EmployeeInfo?>(
                            "Failed to update employee: ${ex.message}",
                            HttpStatus.BAD_REQUEST
                        )
                    )
                }

    @DeleteMapping("deleteEmpInfo/{id}")
    fun deleteEmpInfo(
        @PathVariable id: String
    ): Mono<ResponseEntity<ApiResponse<Void>>> {
        return empInfoService.deleteEmpInfo(id)
            // After delete, emit a 200 OK with your ApiResponse<Void>
            .thenReturn(
                ResponseBuilder.success<Void>(
                    "Employee deleted successfully",
                    null
                )
            )
    }


    /**
     * Get average scores by role
     * @return Mono<ResponseEntity<ApiResponse<List<RoleScoreSummary>?>>>
     */
    @GetMapping("/getAverageScoresByRole")
    fun getAverageScoresByRole(): Mono<ResponseEntity<ApiResponse<List<RoleScoreSummary>?>>> {
        return empInfoService.getAverageScoresByRole()
            .collectList()
            .map { roleScoreSummaryList ->
                ResponseBuilder.success<List<RoleScoreSummary>?>(
                    "Average scores by role retrieved successfully",
                    roleScoreSummaryList
                )
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<List<RoleScoreSummary>?>(
                        "Failed to retrieve average scores by role: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }

    /**
     * Get average scores by
     * @return Mono<ResponseEntity<ApiResponse<List<GenderScoreSummary>?>>>
     */
    @GetMapping("/getAverageScoresByGender")
    fun getAverageScoresByGender(): Mono<ResponseEntity<ApiResponse<List<GenderScoreSummary>?>>> {
        return empInfoService.getAverageScoresByGender()
            .collectList()
            .map { roleScoreSummaryList ->
                ResponseBuilder.success<List<GenderScoreSummary>?>(
                    "Average scores by gender retrieved successfully",
                    roleScoreSummaryList
                )
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<List<GenderScoreSummary>?>(
                        "Failed to retrieve average scores by gender: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }

    }

    @GetMapping("/getDeptByJobWithRanges")
    fun getDeptByJobWithRanges():
            Mono<ResponseEntity<ApiResponse<List<DeptJobRangeAgg>?>>> {
        return empInfoService.getDeptByJobWithRanges()
            .collectList()
            .map { roleScoreSummaryList ->
                ResponseBuilder.success<List<DeptJobRangeAgg>?>(
                    "Average scores by gender retrieved successfully",
                    roleScoreSummaryList
                )
            }
            .onErrorResume { ex ->
                Mono.just(
                    ResponseBuilder.error<List<DeptJobRangeAgg>?>(
                        "Failed to retrieve average scores by gender: ${ex.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }

    }
}