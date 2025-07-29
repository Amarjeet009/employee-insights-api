package com.amarj.repository

import com.amarj.entity.EmployeePersonalDetail
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface EmployeePersonalDetailRepository: ReactiveCrudRepository<EmployeePersonalDetail, Long> {
    fun findByEmployeeId(employeeId: Long): Mono<EmployeePersonalDetail>
    // Additional query methods can be defined here if needed
}