package com.amarj.service


import com.amarj.entity.GradeAndRole
import com.amarj.exception.BadRequestException

import com.amarj.model.GradeAndRoleRequest
import com.amarj.repository.GradeAndRoleRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono



@Service
class GradeAndRoleService(private val gradeAndRoleRepo: GradeAndRoleRepository) {

    fun getAllGradeAndRole(): Flux<GradeAndRole> =
        gradeAndRoleRepo.findAll();


    fun getGradeAndRoleById(id: Long): Mono<GradeAndRole?> =
        gradeAndRoleRepo.findById(id)
            .switchIfEmpty(
                Mono.error(
                    BadRequestException("Grade and Role with ID $id is not available")
                )
            )

    fun saveGradeAndRole(request: List<GradeAndRoleRequest>): Flux<GradeAndRole> {
        val validationFlux = Flux.fromIterable(request)
            .flatMap { req ->
                findByNameOrError(req.roleName)
                    .onErrorResume { ex ->
                        Mono.error(BadRequestException("Duplicate name '${req.roleName}' in batch: ${ex.message}"))
                    }
                    .thenReturn(req) // Only return the request if validation passes
            }

        return validationFlux
            .map { req -> GradeAndRole(
                grade = req.grade,
                roleName = req.roleName,
                salaryRange = req.salaryRange,
                departmentId = req.departmentId
            ) }
            .collectList()
            .flatMapMany { gradeAndRoleRepo.saveAll(it) }
    }


    fun validateUniqueNames(requests: List<GradeAndRoleRequest>): Mono<Void> {
        val validations = requests.map { req ->
            findByNameOrError(req.roleName)
                .onErrorResume { ex ->
                    Mono.error(BadRequestException("Duplicate role name '${req.roleName}' in batch: ${ex.message}"))
                }
        }
        return Flux.merge(validations).then()
    }
    fun findByNameOrError(roleName: String, currentId: Long? = null): Mono<GradeAndRole> =
        gradeAndRoleRepo.findByRoleName(roleName)
            .flatMap { existing ->
                if (currentId == null || existing.id != currentId) {
                    Mono.error(
                        BadRequestException("Role name '$roleName' already exists")
                    )
                } else {
                    Mono.empty() // Same record, allow update
                }
            }

    fun updateGradeAndRole(id: Long, request: GradeAndRoleRequest): Mono<GradeAndRole> =
        gradeAndRoleRepo.findById(id)
            .switchIfEmpty(
                Mono.error(
                    BadRequestException("Grade and Role with ID $id is not available")
                )
            )
            .flatMap { existingRole ->
                findByNameOrError(request.roleName, id)
                    .thenReturn(existingRole.copy(
                        grade = request.grade,
                        roleName = request.roleName,
                        salaryRange = request.salaryRange,
                        departmentId = request.departmentId
                    ))
            }
            .flatMap { updatedRole ->
                gradeAndRoleRepo.save(updatedRole)
            }

    fun deleteGradeAndRoleById(id: Long): Mono<Void> =
        gradeAndRoleRepo.findById(id)
            .switchIfEmpty(
                Mono.error(
                    BadRequestException("Grade and Role with ID $id is not available")
                )
            )
            .flatMap { existingRole ->
                gradeAndRoleRepo.delete(existingRole)
            }
}