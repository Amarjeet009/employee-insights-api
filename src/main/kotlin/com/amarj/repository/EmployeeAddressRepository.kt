package com.amarj.repository

import com.amarj.entity.EmployeeAddress
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface EmployeeAddressRepository: ReactiveCrudRepository<EmployeeAddress, Long> {
    // Additional query methods can be defined here if needed
    fun findByEmployeeIdAndStatus(employeeId: Long, status: Int? = 1): Mono<EmployeeAddress>

}