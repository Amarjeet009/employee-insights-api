package com.amarj.repository

import com.amarj.entity.EmployeeBankingDetail
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface EmployeeBankingDetailRepository : ReactiveCrudRepository<EmployeeBankingDetail, Long> {
    // Additional query methods can be defined here if needed
    fun findByEmpIdAndStatus(empId: Long, status: Int? = 1): Mono<EmployeeBankingDetail>
}