package com.amarj.service

import com.amarj.entity.info.DepartmentAnalytics
import com.amarj.entity.info.DepartmentSalary
import com.amarj.entity.info.EmployeeInfo
import com.amarj.exception.NotFoundException
import com.amarj.repository.EmployeeCustomRepository
import com.amarj.repository.EmployeeInfoRepository
import jakarta.validation.ValidationException
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class EmployeeInfoService(
    val empInfoRepo: EmployeeInfoRepository,
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val empCustomRepo: EmployeeCustomRepository
) {

    fun getAllEmpInfo(): Flux<EmployeeInfo> =
        empInfoRepo.findAll()
            .collectList()
            .flatMapMany { list ->
                Flux.fromIterable(list.sortedBy { it.id?.toIntOrNull() ?: -1 })
            }
            .switchIfEmpty(Mono.error(NotFoundException("Employees Info not found")))

    fun getHighestSalaryPerDepartment(): Flux<DepartmentSalary> =
              empCustomRepo.getHighestSalaryPerDepartment()
                  .collectList()
                  .flatMapMany { list ->
                      Flux.fromIterable(list.sortedBy { it.highestSalary?: -1.0 })
                  }
                  .switchIfEmpty(Mono.error(NotFoundException("Employees Info not found")))


    fun getDepartmentAnalytics(): Flux<DepartmentAnalytics> =
              empCustomRepo.getDepartmentAnalytics()
                  .collectList()
                  .flatMapMany { list ->
                      Flux.fromIterable(list.sortedBy { it.highestSalary?: -1.0 })
                  }
                  .switchIfEmpty(Mono.error(NotFoundException("Records not available")))

    fun createEmpInfo(employeeInfo: List<EmployeeInfo>): Flux<EmployeeInfo> {
        return Flux.fromIterable(employeeInfo)
            .flatMap { info ->
                validateEmployeeInfo(info)
                    .flatMap { empInfoRepo.save(it) }
            }
    }



    fun updateEmpInfo(id: String, empInfo: EmployeeInfo): Mono<EmployeeInfo> {
        return empInfoRepo.findById(id)
            // 1. Fail fast if no record exists
            .switchIfEmpty(
                Mono.error(NotFoundException("EmployeeInfo with id=$id not found"))
            )
            .flatMap { existing ->
                // 2. Merge incoming fields (only non-blank / non-null override)
                val merged = existing.copy(
                    firstName          = empInfo.firstName.takeIf { it.isNotBlank() } ?: existing.firstName,
                    lastName           = empInfo.lastName.takeIf  { it.isNotBlank() } ?: existing.lastName,
                    email              = empInfo.email.takeIf     { it.isNotBlank() } ?: existing.email,
                    phone              = empInfo.phone.takeIf     { it.isNotBlank() } ?: existing.phone,
                    gender             = empInfo.gender.takeIf    { it.isNotBlank() } ?: existing.gender,
                    age                = empInfo.age ?: existing.age,
                    jobTitle           = empInfo.jobTitle.takeIf  { it.isNotBlank() } ?: existing.jobTitle,
                    yearsOfExperience  = empInfo.yearsOfExperience ?: existing.yearsOfExperience,
                    salary             = empInfo.salary ?: existing.salary,
                    department         = empInfo.department.takeIf { it.isNotBlank() } ?: existing.department
                )

                // 3. Validate merged object, then persist
                validateEmployeeInfo(merged)
                    .flatMap { valid -> empInfoRepo.save(valid) }
            }
    }


    private fun validateEmployeeInfo(info: EmployeeInfo): Mono<EmployeeInfo> {
        return Mono.just(info)
            .flatMap {
                when {
                    it.firstName.isBlank() ->
                        Mono.error(ValidationException("firstName must not be blank"))
                    it.lastName.isBlank() ->
                        Mono.error(ValidationException("lastName must not be blank"))
                    it.email.isBlank() ->
                        Mono.error(ValidationException("email must not be blank"))
                    it.phone.isBlank() ->
                        Mono.error(ValidationException("phone must not be blank"))
                    it.gender.isBlank() ->
                        Mono.error(ValidationException("gender must not be blank"))
                    it.age == null ->
                        Mono.error(ValidationException("age must not be null"))
                    it.age > 65 ->
                        Mono.error(ValidationException("age must not exceed 65"))
                    it.jobTitle.isBlank() ->
                        Mono.error(ValidationException("jobTitle must not be blank"))
                    it.yearsOfExperience == null ->
                        Mono.error(ValidationException("yearsOfExperience must not be null"))
                    it.salary == null ->
                        Mono.error(ValidationException("salary must not be null"))
                    it.department.isBlank() ->
                        Mono.error(ValidationException("department must not be blank"))
                    else ->
                        Mono.just(it)
                }
            }
    }

    fun deleteEmpInfo(id: String): Mono<Void> {
        return empInfoRepo.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Employee with id=$id not found")))
            .flatMap { empInfoRepo.deleteById(id) }
    }


}




