package com.amarj.service

import com.amarj.entity.Employee
import com.amarj.entity.EmployeeAddress
import com.amarj.entity.EmployeeBankingDetail
import com.amarj.entity.EmployeePersonalDetail
import com.amarj.exception.NotFoundException
import com.amarj.model.EmpAddressRequestDTO
import com.amarj.model.EmpBankingDetailRequestDTO
import com.amarj.model.EmpPersonalDetailRequestDTO
import com.amarj.model.EmpRequestDTO
import com.amarj.repository.EmployeeAddressRepository
import com.amarj.repository.EmployeeBankingDetailRepository
import com.amarj.repository.EmployeePersonalDetailRepository
import com.amarj.repository.EmployeeRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.time.LocalDate


@Service
class EmployeeService(
    private val empRepo: EmployeeRepository,
    private val empAddressRepo: EmployeeAddressRepository,
    private val empPersonalDetailRepo: EmployeePersonalDetailRepository,
    private val empBankingDetailRepo: EmployeeBankingDetailRepository
) {





    fun saveEmployees(requests: List<EmpRequestDTO>): Flux<Employee> {
        return Flux.fromIterable(requests)
            .flatMap { req ->
                val emailMono = if (req.emailId.isBlank()) {
                    generateUniqueEmailId(req.firstName, req.lastName, empRepo)
                } else {
                    Mono.just(req.emailId)
                }
                val codeMono = if (req.empCode.isBlank()) {
                    generateUniqueEmpCode(empRepo)
                } else {
                    Mono.just(req.empCode)
                }

                Mono.zip(emailMono, codeMono).flatMap { tuple ->
                    val email = tuple.component1()
                    val code = tuple.component2()
                    empRepo.findByEmailId(email).hasElement().flatMap { exists ->
                        if (exists) {
                            Mono.error(NotFoundException("Employee with email $email already exists"))
                        } else {
                            Mono.just(
                                Employee(
                                    firstName = req.firstName,
                                    lastName = req.lastName,
                                    middleName = req.middleName,
                                    gender = req.gender,
                                    emailId = email,
                                    roleId = req.roleId,
                                    empCode = code,
                                    joinedOn = req.joinedOn,
                                    createdAt = LocalDate.now()
                                )
                            )
                        }
                    }
                }
            }
            .collectList()
            .flatMapMany { empList ->
                if (empList.isEmpty()) {
                    Mono.error(NotFoundException("No employees to save"))
                } else {
                    empRepo.saveAll(empList)
                }
            }
    }
    private fun generateUniqueEmailId(firstName: String, lastName: String, empRepo: EmployeeRepository): Mono<String> {
        val baseEmail = "${firstName.lowercase()}.${lastName.lowercase()}@amarj.com"
        fun tryEmail(suffix: Int = 0): Mono<String> {
            val email = if (suffix == 0) baseEmail else "${firstName.lowercase()}.${lastName.lowercase()}$suffix@company.com"
            return empRepo.findByEmailId(email)
                .hasElement()
                .flatMap { exists ->
                    if (exists) tryEmail(suffix + 1) else Mono.just(email)
                }
        }
        return tryEmail()
    }

    fun generateUniqueEmpCode(empRepo: EmployeeRepository): Mono<String> {
        fun randomEmpCode() = (100_000_000..999_999_999).random().toString()
        fun tryGenerate(): Mono<String> {
            val code = randomEmpCode()
            return empRepo.findByEmpCode(code)
                .hasElement()
                .flatMap { exists ->
                    if (exists) tryGenerate() else Mono.just(code)
                }
        }
        return tryGenerate()
    }

    fun updateEmployee(id: Long, employee: EmpRequestDTO): Mono<Employee> {
        return empRepo.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Employee with ID $id not found")))
            .flatMap { existingEmployee ->
                empRepo.findByEmailId(existingEmployee.emailId)
                    .filter { it.id != id }
                    .hasElement()
                    .flatMap { emailExists ->
                        if (emailExists) {
                            Mono.error(NotFoundException("Email ID '${existingEmployee.emailId}' already exists for another employee"))
                        } else {
                            empRepo.findByEmpCode(existingEmployee.empCode)
                                .filter { it.id != id }
                                .hasElement()
                                .flatMap { codeExists ->
                                    if (codeExists) {
                                        Mono.error(NotFoundException("EmpCode '${existingEmployee.empCode}' already exists for another employee"))
                                    } else {
                                        val updatedEmployee = Employee(
                                            id = existingEmployee.id,
                                            firstName = existingEmployee.firstName,
                                            lastName = existingEmployee.lastName,
                                            middleName = employee.middleName,
                                            gender = existingEmployee.gender,
                                            emailId = existingEmployee.emailId,
                                            roleId = employee.roleId,
                                            empCode = existingEmployee.empCode,
                                            joinedOn = existingEmployee.joinedOn,
                                            createdAt = existingEmployee.createdAt,
                                            updatedAt = LocalDate.now(),
                                            status = employee.status
                                        )
                                        empRepo.save(updatedEmployee)
                                    }
                                }
                        }
                    }
            }
    }



    /**
     * Deletes an employee by ID.
     * If the employee does not exist, it throws a NotFoundException.
     */

    fun deleteEmployeeById(id: Long): Mono<Void> {
        return empRepo.findById(id)
            .switchIfEmpty(
                Mono.error(
                    NotFoundException("Employee with ID $id not found")
                )
            )
            .flatMap { existingEmployee ->
                empRepo.delete(existingEmployee)
            }
    }


    fun saveEmployeeAddress(empAddress: EmpAddressRequestDTO): Mono<EmployeeAddress> {
        val missingFields = mutableListOf<String>()

        if (empAddress.countryCode.isBlank()) missingFields.add("countryCode")
        if (empAddress.phoneNumber.isBlank()) missingFields.add("phoneNumber")
        if (empAddress.emergencyContactName.toString().isBlank()) missingFields.add("emergencyContactName")
        if (empAddress.emergencyContactRelation.toString().isBlank()) missingFields.add("emergencyContactRelation")
        if (empAddress.emergencyContactNumber.isBlank()) missingFields.add("emergencyContactNumber")
        if (empAddress.houseNumber.toString().isBlank()) missingFields.add("houseNumber")
        if (empAddress.street.isBlank()) missingFields.add("street")
        if (empAddress.city.isBlank()) missingFields.add("city")
        if (empAddress.state.isBlank()) missingFields.add("state")
        if (empAddress.country.isEmpty()) missingFields.add("country")
        if (empAddress.postalCode.isEmpty()) missingFields.add("postalCode")

        if (missingFields.isNotEmpty()) {
            return Mono.error(IllegalArgumentException("Missing required fields: ${missingFields.joinToString(", ")}"))
        }

        return empRepo.findById(empAddress.employeeId)
            .switchIfEmpty(Mono.error(NotFoundException("Employee with ID ${empAddress.employeeId} not found")))
            .flatMap {
                val employeeAddress = EmployeeAddress(
                    employeeId = empAddress.employeeId,
                    countryCode = empAddress.countryCode,
                    phoneNumber = empAddress.phoneNumber,
                    alternateNumber = empAddress.alternateNumber,
                    emergencyContactName = empAddress.emergencyContactName,
                    emergencyContactRelation = empAddress.emergencyContactRelation,
                    emergencyContactNumber = empAddress.emergencyContactNumber,
                    houseNumber = empAddress.houseNumber!!,
                    street = empAddress.street,
                    city = empAddress.city,
                    state = empAddress.state,
                    country = empAddress.country,
                    postalCode = empAddress.postalCode,
                    updatedAt = LocalDate.now(),
                    status = empAddress.status ?: 1
                )
                empAddressRepo.save(employeeAddress)
            }
    }


    fun updateEmployeeAddress(employeeId:Long,empAddress: EmpAddressRequestDTO): Mono<EmployeeAddress> {
        return empAddressRepo.findByEmployeeIdAndStatus(employeeId, 1)
            .flatMap { existing ->
                val updated = existing.copy(status = 0)
                empAddressRepo.save(updated)
            }
            .then(
                empAddressRepo.save(
                    EmployeeAddress(
                        employeeId = empAddress.employeeId,
                        countryCode = empAddress.countryCode,
                        phoneNumber = empAddress.phoneNumber,
                        alternateNumber = empAddress.alternateNumber,
                        emergencyContactName = empAddress.emergencyContactName,
                        emergencyContactRelation = empAddress.emergencyContactRelation,
                        emergencyContactNumber = empAddress.emergencyContactNumber,
                        houseNumber = empAddress.houseNumber,
                        street = empAddress.street,
                        city = empAddress.city,
                        state = empAddress.state,
                        country = empAddress.country,
                        postalCode = empAddress.postalCode,
                        updatedAt = LocalDate.now(),
                        status = empAddress.status ?: 1
                    )
                )
            )
    }


    fun saveEmployeePersonalDetail(request: EmpPersonalDetailRequestDTO): Mono<EmployeePersonalDetail> {
        val missingFields = mutableListOf<String>()

        if (request.empDOB == null) missingFields.add("empDOB")
        if (request.personalEmailId == null) missingFields.add("personalEmailId")
        if (request.fatherName == null) missingFields.add("fatherName")
        if (request.fatherDOB == null) missingFields.add("fatherDOB")
        if (request.fatherOccupation == null) missingFields.add("fatherOccupation")
        if (request.motherName == null) missingFields.add("motherName")
        if (request.motherDOB == null) missingFields.add("motherDOB")
        if (request.motherOccupation == null) missingFields.add("motherOccupation")
        if (request.isMarried == null) missingFields.add("isMarried")

        if (missingFields.isNotEmpty()) {
            return Mono.error(IllegalArgumentException("Missing required fields: ${missingFields.joinToString(", ")}"))
        }
        if (request.isMarried == 1 && request.marriageDate == null) {
            return Mono.error(IllegalArgumentException("Marriage date is required if married"))
        }
        if (request.isMarried == 1 && request.spouseName == null) {
            return Mono.error(IllegalArgumentException("Spouse name is required if married"))
        }
        if (request.isMarried == 1 && request.spouseDOB == null) {
            return Mono.error(IllegalArgumentException("Spouse DOB is required if married"))
        }
        if (request.isMarried == 1 && request.spouseOccupation == null) {
            return Mono.error(IllegalArgumentException("Spouse occupation is required if married"))
        }
        if (request.childrenCount == null || request.childrenCount < 0 || request.childrenCount > 3) {
            return Mono.error(IllegalArgumentException("Children count must be between 0 and 3"))
        }
        if (request.childrenCount > 0) {
            if (request.childrenCount == 1 && (request.childFirstName == null || request.childFirstDOB == null)) {
                return Mono.error(IllegalArgumentException("Child first name and DOB are required for 1 child"))
            }
            if (request.childrenCount == 2 && (request.childSecondName == null || request.childSecondDOB == null)) {
                return Mono.error(IllegalArgumentException("Child second name and DOB are required for 2 children"))
            }

            if (request.childrenCount == 3 && (request.childThirdName == null || request.childThirdDOB == null)) {
                return Mono.error(IllegalArgumentException("Child third name and DOB are required for 3 children"))
            }

        }


        return empRepo.findById(request.employeeId)
            .switchIfEmpty(Mono.error(NotFoundException("Employee with ID ${request.employeeId} not found")))
            .flatMap {
                val personalDetail = EmployeePersonalDetail(
                    employeeId = request.employeeId,
                    empDOB = request.empDOB!!,
                    personalEmailId = request.personalEmailId!!,
                    fatherName = request.fatherName!!,
                    fatherDOB = request.fatherDOB!!,
                    fatherOccupation = request.fatherOccupation!!,
                    motherName = request.motherName!!,
                    motherDOB = request.motherDOB!!,
                    motherOccupation = request.motherOccupation!!,
                    isMarried = request.isMarried!!,
                    marriageDate = request.marriageDate,
                    spouseName = request.spouseName,
                    spouseDOB = request.spouseDOB,
                    spouseOccupation = request.spouseOccupation,
                    childrenCount = request.childrenCount ?: 0,
                    childFirstName = request.childFirstName,
                    childFirstDOB = request.childFirstDOB,
                    childSecondName = request.childSecondName,
                    childSecondDOB = request.childSecondDOB,
                    childThirdName = request.childThirdName,
                    childThirdDOB = request.childThirdDOB,
                    updatedAt = LocalDate.now(),
                    status = request.status ?: 1
                )
                empPersonalDetailRepo.save(personalDetail)
            }
    }

    fun updateEmployeePersonalDetail(employeeId:Long,request: EmpPersonalDetailRequestDTO): Mono<EmployeePersonalDetail> {
        return empPersonalDetailRepo.findByEmployeeId(employeeId)
            .switchIfEmpty(Mono.error(NotFoundException("Employee personal details for ID ${request.employeeId} not found")))
            .flatMap { existing ->
                val updatedDetail = existing.copy(
                    empDOB = request.empDOB ?: existing.empDOB,
                    personalEmailId = request.personalEmailId ?: existing.personalEmailId,
                    fatherName = request.fatherName ?: existing.fatherName,
                    fatherDOB = request.fatherDOB ?: existing.fatherDOB,
                    fatherOccupation = request.fatherOccupation ?: existing.fatherOccupation,
                    motherName = request.motherName ?: existing.motherName,
                    motherDOB = request.motherDOB ?: existing.motherDOB,
                    motherOccupation = request.motherOccupation ?: existing.motherOccupation,
                    isMarried = request.isMarried ?: existing.isMarried,
                    marriageDate = request.marriageDate ?: existing.marriageDate,
                    spouseName = request.spouseName ?: existing.spouseName,
                    spouseDOB = request.spouseDOB ?: existing.spouseDOB,
                    spouseOccupation = request.spouseOccupation ?: existing.spouseOccupation,
                    childrenCount = request.childrenCount ?: existing.childrenCount,
                    childFirstName = request.childFirstName ?: existing.childFirstName,
                    childFirstDOB = request.childFirstDOB ?: existing.childFirstDOB,
                    childSecondName = request.childSecondName ?: existing.childSecondName,
                    childSecondDOB = request.childSecondDOB ?: existing.childSecondDOB,
                    childThirdName = request.childThirdName ?: existing.childThirdName,
                    childThirdDOB = request.childThirdDOB ?: existing.childThirdDOB,
                    updatedAt = LocalDate.now(),
                    status = request.status ?: existing.status
                )

                empPersonalDetailRepo.save(updatedDetail)
            }
    }

    fun saveEmployeeBankingDetail(request: EmpBankingDetailRequestDTO): Mono<EmployeeBankingDetail> {
        val missingFields = mutableListOf<String>()
        if (request.aadhaarNumber == null) missingFields.add("aadhaarNumber")
        if (request.panNumber == null) missingFields.add("panNumber")
        if (request.bankName == null) missingFields.add("bankName")
        if (request.accountNumber == null) missingFields.add("accountNumber")
        if (request.ifscCode == null) missingFields.add("ifscCode")
        if (request.branch == null) missingFields.add("branch")

        if (missingFields.isNotEmpty()) {
            return Mono.error(IllegalArgumentException("Missing required fields: ${missingFields.joinToString(", ")}"))
        }

        val entity = EmployeeBankingDetail(
            empId = request.empId,
            aadhaarNumber = request.aadhaarNumber,
            panNumber = request.panNumber,
            uanNumber = request.uanNumber,
            pfNumber = request.pfNumber,
            workDays = request.workDays ?: 22,
            bankName = request.bankName,
            accountNumber = request.accountNumber,
            ifscCode = request.ifscCode,
            branch = request.branch,
            status = request.status ?: 1
        )

        return empBankingDetailRepo.save(entity)
    }


    fun updateEmployeeBankingDetail(empId: Long, request: EmpBankingDetailRequestDTO): Mono<EmployeeBankingDetail> {
        return empBankingDetailRepo.findByEmpIdAndStatus(empId, 1)
            .switchIfEmpty(Mono.error(NotFoundException("No record found with ID $empId")))
            .flatMap { existing ->
                val updated = existing.copy(status = 0)
                empBankingDetailRepo.save(updated)
            }
            .then(
                empBankingDetailRepo.save(
                    EmployeeBankingDetail(
                        empId = request.empId,
                        aadhaarNumber = request.aadhaarNumber,
                        panNumber = request.panNumber,
                        uanNumber = request.uanNumber,
                        pfNumber = request.pfNumber,
                        workDays = request.workDays ?: 22,
                        bankName = request.bankName,
                        accountNumber = request.accountNumber,
                        ifscCode = request.ifscCode,
                        branch = request.branch,
                        status = request.status ?: 1
                    )
                )
            )
    }
}


