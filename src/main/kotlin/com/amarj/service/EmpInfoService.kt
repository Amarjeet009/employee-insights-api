package com.amarj.service

import com.amarj.entity.Employee
import com.amarj.exception.NotFoundException
import com.amarj.model.EmpDetailsRequestDTO
import com.amarj.repository.EmployeeAddressRepository
import com.amarj.repository.EmployeeBankingDetailRepository
import com.amarj.repository.EmployeePersonalDetailRepository
import com.amarj.repository.EmployeeRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class EmpInfoService(
    private val empRepo: EmployeeRepository,
    private val empAddressRepo: EmployeeAddressRepository,
    private val empPersonalDetailRepo: EmployeePersonalDetailRepository,
    private val empBankingDetailRepo: EmployeeBankingDetailRepository
) {

    fun getAllEmployees(): Flux<Employee> =
        empRepo.findAll()
            .sort { e1, e2 -> (e1.id?:-1).compareTo(e2.id?: -1) }
            .switchIfEmpty(
                Mono.error(NotFoundException("Employees not found"))
            )

    /**
     * Finds an employee by id, first name, last name, empCode or email ID.
     * If no employee is found, it throws a NotFoundException.
     */
    fun findEmployeeDetails(request: EmpDetailsRequestDTO): Flux<Employee> {
        return when {
            request.id != null -> empRepo.findById(request.id)
                .switchIfEmpty(Mono.error(NotFoundException("Employee with ID ${request.id} not found")))
                .flux()

            request.empCode != null -> empRepo.findByEmpCode(request.empCode)
                .switchIfEmpty(Mono.error(NotFoundException("Employee with code ${request.empCode} not found")))
                .flux()

            request.emailId != null -> empRepo.findByEmailId(request.emailId)
                .switchIfEmpty(Mono.error(NotFoundException("Employee with email ${request.emailId} not found")))
                .flux()

            request.firstName != null -> empRepo.findByFirstName(request.firstName)
                .switchIfEmpty(Flux.error(NotFoundException("Employee with first name ${request.firstName} not found")))

            request.lastName != null -> empRepo.findByLastName(request.lastName)
                .switchIfEmpty(Flux.error(NotFoundException("Employee with last name ${request.lastName} not found")))

            else -> Flux.error(NotFoundException("No valid search parameter provided"))
        }
    }
}