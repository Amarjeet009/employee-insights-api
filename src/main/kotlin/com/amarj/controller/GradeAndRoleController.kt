package com.amarj.controller

import com.amarj.entity.GradeAndRole
import com.amarj.model.GradeAndRoleRequestDTO
import com.amarj.response.ApiResponse
import com.amarj.response.ResponseBuilder
import com.amarj.service.GradeAndRoleService
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
@RequestMapping("/api/v1/gradeAndRole")
class GradeAndRoleController(private val gradeAndRoleService: GradeAndRoleService) {

    @GetMapping("/getAllGradeAndRole")
    fun getAllGradeAndRole(): Mono<ResponseEntity<ApiResponse<List<GradeAndRole?>?>>> =
        gradeAndRoleService.getAllGradeAndRole()
            .collectList()
            .map { roles ->
                ResponseBuilder.success<List<GradeAndRole?>?>("Roles fetched successfully", roles)
            }
            .onErrorResume { ex ->
                Mono.just(ResponseBuilder.error<List<GradeAndRole?>?>("Failed to fetch roles: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR))
            }

    @GetMapping("/getGradeAndRoleById/{id}")
    fun getGradeAndRoleById(@PathVariable id: Long): Mono<ResponseEntity<ApiResponse<GradeAndRole?>>> =
        gradeAndRoleService.getGradeAndRoleById(id)
            .map { role ->
                ResponseBuilder.success<GradeAndRole?>("Role with ID $id fetched", role)
            }
            .onErrorResume { ex ->
                Mono.just(ResponseBuilder.error<GradeAndRole?>("Role not found: ${ex.message}", HttpStatus.NOT_FOUND))
            }
    @PostMapping("/saveGradeAndRole")
    fun saveGradeAndRole(@Valid @RequestBody requests: List<GradeAndRoleRequestDTO>): Mono<ResponseEntity<ApiResponse<List<GradeAndRole?>?>>> =
        gradeAndRoleService.saveGradeAndRole(requests)
            .collectList()
            .map { savedList ->
                ResponseBuilder.success<List<GradeAndRole?>?>("${savedList.size} roles created successfully", savedList)
            }
            .onErrorResume { ex ->
                Mono.just(ResponseBuilder.error<List<GradeAndRole?>?>("Failed to create roles: ${ex.message}", HttpStatus.BAD_REQUEST))
            }

    @PutMapping("/updateGradeAndRole/{id}")
    fun updateGradeAndRole(@PathVariable id: Long, @Valid @RequestBody request: GradeAndRoleRequestDTO): Mono<ResponseEntity<ApiResponse<GradeAndRole?>>> =
        gradeAndRoleService.updateGradeAndRole(id,request)
            .map { updated ->
                ResponseBuilder.success<GradeAndRole?>("Department with ID ${updated.id} updated", updated)
            }
            .onErrorResume { ex ->
                Mono.just(ResponseBuilder.error<GradeAndRole?>("Failed to update department: ${ex.message}", HttpStatus.BAD_REQUEST))
            }

    @DeleteMapping("/deleteGradeAndRole/{id}")
    fun deleteGradeAndRole(@PathVariable id: Long): Mono<ResponseEntity<ApiResponse<Unit?>>> =
        gradeAndRoleService.deleteGradeAndRoleById(id)
            .then(Mono.just(ResponseBuilder.success<Unit?>("Role with ID $id deleted successfully", null)))
            .onErrorResume { ex ->
                Mono.just(ResponseBuilder.error<Unit?>("Failed to delete role: ${ex.message}", HttpStatus.BAD_REQUEST))
            }
}