package com.amarj.controller

import com.amarj.entity.Department
import com.amarj.model.DepartmentRequest
import com.amarj.response.ApiResponse
import com.amarj.response.ResponseBuilder
import com.amarj.service.DepartmentService
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
@RequestMapping("/api/department")
class DepartmentController (
    private val departmentService: DepartmentService
) {

    @GetMapping("/getAllDepartments")
    fun getAllDepartments(): Mono<ResponseEntity<ApiResponse<List<Department?>?>>> =
        departmentService.getAllDepartment()
            .collectList()
            .map { departments ->
                ResponseBuilder.success<List<Department?>?>("Departments fetched successfully", departments)
            }
            .onErrorResume { ex ->
                Mono.just(ResponseBuilder.error<List<Department?>?>("Failed to fetch departments: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR))
            }

    @GetMapping("/getDepartmentById/{dept_id}")
    fun getDepartmentById(@PathVariable dept_id: Long): Mono<ResponseEntity<ApiResponse<Department?>>> =
        departmentService.getDepartmentById(dept_id)
            .map { dept ->
                ResponseBuilder.success<Department?>("Department with ID $dept_id fetched", dept)
            }
            .onErrorResume { ex ->
                Mono.just(ResponseBuilder.error<Department?>("Department not found: ${ex.message}", HttpStatus.NOT_FOUND))
            }

    @PostMapping("/saveDepartments")
    fun saveDepartments(@Valid @RequestBody requests: List<DepartmentRequest>): Mono<ResponseEntity<ApiResponse<List<Department?>?>>> =
        departmentService.saveDepartments(requests)
            .collectList()
            .map { savedList ->
                ResponseBuilder.success<List<Department?>?>("${savedList.size} departments created successfully", savedList)
            }
            .onErrorResume { ex ->
                Mono.just(ResponseBuilder.error<List<Department?>?>("Failed to create departments: ${ex.message}", HttpStatus.BAD_REQUEST))
            }



    @PutMapping("/updateDepartment/{id}")
    fun updateDepartment(@PathVariable id: Long, @Valid @RequestBody departmentRequest: DepartmentRequest): Mono<ResponseEntity<ApiResponse<Department?>>> =
        departmentService.updateDepartment(id, departmentRequest)
            .map { updated ->
                ResponseBuilder.success<Department?>("Department with ID ${updated.id} updated", updated)
            }
            .onErrorResume { ex ->
                Mono.just(ResponseBuilder.error<Department?>("Failed to update department: ${ex.message}", HttpStatus.BAD_REQUEST))
            }

    @DeleteMapping("/deleteDepartmentById/{id}")
    fun deleteDepartmentById(@PathVariable id: Long): Mono<ResponseEntity<ApiResponse<Unit?>>> =
        departmentService.deleteDepartmentById(id)
            .thenReturn(ResponseBuilder.success<Unit?>("Department with ID $id deleted successfully"))
            .onErrorResume {ex ->
                Mono.just(ResponseBuilder.error<Unit?>("Failed to delete Department with ID $id: ${ex.message}", HttpStatus.NOT_FOUND))
            }




}