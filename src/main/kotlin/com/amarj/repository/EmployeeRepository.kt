package com.amarj.repository

import com.amarj.entity.Employee
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface EmployeeRepository : ReactiveCrudRepository<Employee, Long>{
    fun findByEmpCode(empCode: String): Mono<Employee>
    fun findByEmailId(emailId: String?): Mono<Employee>
    fun findByFirstName(firstName: String?): Flux<Employee>
    fun findByLastName(lastName: String?): Flux<Employee>
}
