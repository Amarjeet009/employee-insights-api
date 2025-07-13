package com.amarj.repository

import com.amarj.entity.Department
import com.amarj.entity.GradeAndRole
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface GradeAndRoleRepository: ReactiveCrudRepository<GradeAndRole, Long> {
    fun findByRoleName(roleName: String): Mono<GradeAndRole>
}