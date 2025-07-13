package com.amarj.service

import com.amarj.entity.GradeAndRole
import com.amarj.exception.BadRequestException
import com.amarj.model.GradeAndRoleRequest
import com.amarj.repository.GradeAndRoleRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest
class GradeAndRoleServiceTest {

    private lateinit var gradeAndRoleRepo: GradeAndRoleRepository
    private lateinit var service: GradeAndRoleService

    @BeforeEach
    fun setup() {
        gradeAndRoleRepo = mock(GradeAndRoleRepository::class.java)
        service = GradeAndRoleService(gradeAndRoleRepo)
    }

    @Test
    fun `getAllGradeAndRole returns all roles`() {
        val roles = listOf(
            GradeAndRole(1L, 1L, "Developer", "10k-20k", 1L),
            GradeAndRole(2L, 2L, "Manager", "20k-30k", 2L)
        )
        doReturn(Flux.fromIterable(roles)).`when`(gradeAndRoleRepo).findAll()

        StepVerifier.create(service.getAllGradeAndRole())
            .expectNextSequence(roles)
            .verifyComplete()
    }

    @Test
    fun `getGradeAndRoleById returns role when found`() {
        val role = GradeAndRole(1L, 1L, "Developer", "10k-20k", 1L)
        doReturn(Mono.just(role)).`when`(gradeAndRoleRepo).findById(1L)

        StepVerifier.create(service.getGradeAndRoleById(1L))
            .expectNext(role)
            .verifyComplete()
    }

    @Test
    fun `getGradeAndRoleById returns error when not found`() {
        doReturn(Mono.empty<GradeAndRole>()).`when`(gradeAndRoleRepo).findById(1L)

        StepVerifier.create(service.getGradeAndRoleById(1L))
            .expectErrorMatches { it is BadRequestException && it.message!!.contains("not available") }
            .verify()
    }

    @Test
    fun `saveGradeAndRole saves valid roles`() {
        val req = GradeAndRoleRequest(1L, "Developer", "10k-20k", 1L)
        val entity = GradeAndRole(null, req.grade, req.roleName, req.salaryRange, req.departmentId)
        doReturn(Mono.empty<GradeAndRole>()).`when`(gradeAndRoleRepo).findByRoleName(req.roleName)
        doReturn(Flux.just(entity.copy(id = 1L))).`when`(gradeAndRoleRepo).saveAll(any<List<GradeAndRole>>())

        StepVerifier.create(service.saveGradeAndRole(listOf(req)))
            .expectNextMatches { it.roleName == "Developer" }
            .verifyComplete()
    }

    @Test
    fun `saveGradeAndRole fails on duplicate role name`() {
        val req = GradeAndRoleRequest(1L, "Developer", "10k-20k", 1L)
        val existing = GradeAndRole(2L, 1L, "Developer", "10k-20k", 1L)
        doReturn(Mono.just(existing)).`when`(gradeAndRoleRepo).findByRoleName(req.roleName)

        StepVerifier.create(service.saveGradeAndRole(listOf(req)))
            .expectErrorMatches { it is BadRequestException && it.message!!.contains("already exists") }
            .verify()
    }

    @Test
    fun `updateGradeAndRole updates when valid`() {
        val req = GradeAndRoleRequest(2L, "Manager", "20k-30k", 2L)
        val existing = GradeAndRole(1L, 1L, "Developer", "10k-20k", 1L)
        val updated = existing.copy(
            grade = req.grade,
            roleName = req.roleName,
            salaryRange = req.salaryRange,
            departmentId = req.departmentId
        )
        doReturn(Mono.just(existing)).`when`(gradeAndRoleRepo).findById(1L)
        doReturn(Mono.empty<GradeAndRole>()).`when`(gradeAndRoleRepo).findByRoleName(req.roleName)
        doReturn(Mono.just(updated.copy(id = 1L))).`when`(gradeAndRoleRepo).save(updated)

        StepVerifier.create(service.updateGradeAndRole(1L, req))
            .expectNextMatches { it.roleName == "Manager" }
            .verifyComplete()
    }

    @Test
    fun `deleteGradeAndRoleById deletes when found`() {
        val existing = GradeAndRole(1L, 1L, "Developer", "10k-20k", 1L)
        doReturn(Mono.just(existing)).`when`(gradeAndRoleRepo).findById(1L)
        doReturn(Mono.empty<Void>()).`when`(gradeAndRoleRepo).delete(existing)

        StepVerifier.create(service.deleteGradeAndRoleById(1L))
            .verifyComplete()
    }

    @Test
    fun `deleteGradeAndRoleById returns error when not found`() {
        doReturn(Mono.empty<GradeAndRole>()).`when`(gradeAndRoleRepo).findById(1L)

        StepVerifier.create(service.deleteGradeAndRoleById(1L))
            .expectErrorMatches { it is BadRequestException }
            .verify()
    }
}
