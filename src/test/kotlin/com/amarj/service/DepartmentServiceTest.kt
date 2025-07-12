package com.amarj.service

import com.amarj.entity.Department
import com.amarj.exception.NotFoundException
import com.amarj.model.DepartmentRequest
import com.amarj.repository.DepartmentRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(SpringExtension::class)
@SpringBootTest
class DepartmentServiceTest {

    @MockitoBean
    private lateinit var departmentRepo: DepartmentRepository

    @Autowired
    private lateinit var departmentService: DepartmentService

    @Test
    fun `getDepartmentById returns department when found`() {
        val department = Department(id = 1L, name = "IT", status = 1)
        `when`(departmentRepo.findById(1)).thenReturn(Mono.just(department))

        StepVerifier.create(departmentService.getDepartmentById(1))
            .expectNext(department)
            .verifyComplete()
    }

    @Test
    fun `getDepartmentById returns error when not found`() {
        `when`(departmentRepo.findById(2)).thenReturn(Mono.empty())

        StepVerifier.create(departmentService.getDepartmentById(2))
            .expectError(NotFoundException::class.java)
            .verify()
    }

    @Test
    fun `getAllDepartment returns all departments`() {
        val departments = listOf(
            Department(id = 1L, name = "IT", status = 1),
            Department(id = 2L, name = "HR", status = 1)
        )
        `when`(departmentRepo.findAll()).thenReturn(Flux.fromIterable(departments))

        StepVerifier.create(departmentService.getAllDepartment())
            .expectNextSequence(departments)
            .verifyComplete()
    }

    @Test
    fun `saveDepartments saves batch of departments`() {
        val requests = listOf(
            DepartmentRequest(name = "Finance", status = 1),
            DepartmentRequest(name = "Marketing", status = 1)
        )
        val departments = listOf(
            Department(id = null, name = "Finance", status = 1),
            Department(id = null, name = "Marketing", status = 1)
        )
        // Mock findByName to return Mono.empty() for both names (no duplicates)
        `when`(departmentRepo.findByName("Finance")).thenReturn(Mono.empty())
        `when`(departmentRepo.findByName("Marketing")).thenReturn(Mono.empty())
        // Mock saveAll to return Flux of departments
        `when`(departmentRepo.saveAll(departments)).thenReturn(Flux.fromIterable(departments))

        StepVerifier.create(departmentService.saveDepartments(requests))
            .expectNextSequence(departments)
            .verifyComplete()
    }

    @Test
    fun `updateDepartment updates department when name is changed and not duplicate`() {
        val existing = Department(id = 1, name = "IT", status = 1)
        val request = DepartmentRequest(name = "Finance", status = 0)
        val updated = Department(id = 1, name = "Finance", status = 0)

        `when`(departmentRepo.findById(1)).thenReturn(Mono.just(existing))
        `when`(departmentRepo.findByName("Finance")).thenReturn(Mono.empty())
        `when`(departmentRepo.save(updated)).thenReturn(Mono.just(updated))

        StepVerifier.create(departmentService.updateDepartment(1, request))
            .expectNext(updated)
            .verifyComplete()
    }

    @Test
    fun `updateDepartment returns error when new name is duplicate`() {
        val existing = Department(id = 1, name = "IT", status = 1)
        val request = DepartmentRequest(name = "HR", status = 1)
        val duplicate = Department(id = 2, name = "HR", status = 1)

        `when`(departmentRepo.findById(1)).thenReturn(Mono.just(existing))
        `when`(departmentRepo.findByName("HR")).thenReturn(Mono.just(duplicate))

        StepVerifier.create(departmentService.updateDepartment(1, request))
            .expectErrorMessage("Department name 'HR' already exists")
            .verify()
    }

    @Test
    fun `updateDepartment updates department when name is unchanged`() {
        val existing = Department(id = 1, name = "IT", status = 1)
        val request = DepartmentRequest(name = "IT", status = 2)
        val updated = Department(id = 1, name = "IT", status = 2)

        `when`(departmentRepo.findById(1)).thenReturn(Mono.just(existing))
        `when`(departmentRepo.save(updated)).thenReturn(Mono.just(updated))

        StepVerifier.create(departmentService.updateDepartment(1, request))
            .expectNext(updated)
            .verifyComplete()
    }

    @Test
    fun `deleteDepartmentById deletes department when found`() {
        val department = Department(id = 1L, name = "IT", status = 1)
        `when`(departmentRepo.findById(1)).thenReturn(Mono.just(department))
        `when`(departmentRepo.delete(department)).thenReturn(Mono.empty())

        StepVerifier.create(departmentService.deleteDepartmentById(1))
            .verifyComplete()
    }

    @Test
    fun `deleteDepartmentById returns error when department not found`() {
        `when`(departmentRepo.findById(2)).thenReturn(Mono.empty())

        StepVerifier.create(departmentService.deleteDepartmentById(2))
            .expectError(NotFoundException::class.java)
            .verify()
    }
}