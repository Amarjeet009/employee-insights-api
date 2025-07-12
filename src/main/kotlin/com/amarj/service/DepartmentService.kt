package com.amarj.service

import com.amarj.entity.Department
import com.amarj.exception.BadRequestException
import com.amarj.repository.DepartmentRepository
import  com.amarj.exception.NotFoundException
import com.amarj.model.DepartmentRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class DepartmentService(
    private val departmentRepo: DepartmentRepository

) {


    fun getAllDepartment(): Flux<Department> =
        departmentRepo.findAll();

    fun getDepartmentById(id: Long): Mono<Department> =
        departmentRepo.findById(id)
            .switchIfEmpty(
                Mono.error(
                    NotFoundException("Department with $id is not available")
                )
            );

    fun saveDepartments(requests: List<DepartmentRequest>): Flux<Department> {
        val validationFlux = Flux.fromIterable(requests)
            .flatMap { req ->
                findByNameOrError(req.name)
                    .onErrorResume { ex ->
                        Mono.error(BadRequestException("Duplicate name '${req.name}' in batch: ${ex.message}"))
                    }
                    .thenReturn(req) // Only return the request if validation passes
            }

        return validationFlux
            .map { req -> Department(name = req.name, status = req.status) }
            .collectList()
            .flatMapMany { departmentRepo.saveAll(it) }
    }


    fun validateUniqueNames(requests: List<DepartmentRequest>): Mono<Void> {
        val validations = requests.map { req ->
            findByNameOrError(req.name)
                .onErrorResume { ex ->
                    Mono.error(BadRequestException("Duplicate name '${req.name}' in batch: ${ex.message}"))
                }
        }
        return Flux.merge(validations).then()
    }


    fun findByNameOrError(name: String, currentId: Long? = null): Mono<Department> =
        departmentRepo.findByName(name)
            .flatMap { existing ->
                if (currentId == null || existing.id != currentId) {
                    Mono.error(
                        BadRequestException("Department name '$name' already exists")
                    )
                } else {
                    Mono.empty() // Same record, allow update
                }
            }


    fun updateDepartment(id: Long, departmentRequest: DepartmentRequest): Mono<Department> =
        getDepartmentById(id).flatMap { existingDepartment ->
            val isNameChanged = departmentRequest.name != existingDepartment.name

            val validateName = if (isNameChanged) {
                findByNameOrErrorOnUpdate(departmentRequest.name, id)
            } else {
                Mono.empty()
            }
            validateName.switchIfEmpty(
                departmentRepo.save(
                    existingDepartment.copy(
                        name = departmentRequest.name,
                        status = departmentRequest.status
                    )
                )
            )
        }

    private fun findByNameOrErrorOnUpdate(name: String, currentId: Long): Mono<Department> {
        return departmentRepo.findByName(name)
            .flatMap { existing ->
                if (existing.id != currentId) {
                    Mono.error(BadRequestException("Department name '$name' already exists"))
                } else {
                    Mono.empty() // Same record, allow update
                }
            }
    }

    fun deleteDepartmentById(id: Long): Mono<Void> =
        getDepartmentById(id)
            .flatMap { foundDepartment ->
                departmentRepo.delete(foundDepartment);
            }

}