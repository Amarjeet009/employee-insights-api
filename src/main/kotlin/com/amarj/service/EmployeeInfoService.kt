package com.amarj.service

import com.amarj.constants.ShareConstants
import com.amarj.entity.info.DepartmentAnalytics
import com.amarj.entity.info.DepartmentSalary
import com.amarj.entity.info.DeptJobRangeAgg
import com.amarj.entity.info.EmployeeInfo
import com.amarj.entity.info.analytics.GenderScoreSummary
import com.amarj.entity.info.analytics.RoleScoreSummary
import com.amarj.exception.NotFoundException
import com.amarj.repository.EmployeeCustomRepository
import com.amarj.repository.EmployeeInfoRepository
import jakarta.validation.ValidationException
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.*
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class EmployeeInfoService(
    val empInfoRepo: EmployeeInfoRepository,
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val empCustomRepo: EmployeeCustomRepository,
    private val constants: ShareConstants
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

    /**
     * This function aggregates employee scores by role and calculates the average scores for performance, employee satisfaction, and manager feedback.
     * It uses MongoDB's aggregation framework to group the data by role and compute the averages.
     * The result is returned as a Flux of RoleScoreSummary objects, which contain the role, average performance score, average employee score, and average manager score.
     */
    fun getAverageScoresByRole(): Flux<RoleScoreSummary> {
        val groupStage = Aggregation.group("role")
            .avg("performanace_score").`as`("avgPerformance")
            .sum("performanace_score").`as`("totalPerformance")
            .avg("emp_score").`as`("avgEmpScore")
            .sum("emp_score").`as`("totalEmpScore")
            .avg("manager_score").`as`("avgManagerScore")
            .sum("manager_score").`as`("totalManagerScore")

        val projectStage = Aggregation.project()
            .and("_id").`as`("role")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("avgPerformance").place(2)).then(0.0)).`as`("avgPerformance")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("avgEmpScore").place(2)).then(0.0)).`as`("avgEmpScore")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("avgManagerScore").place(2)).then(0.0)).`as`("avgManagerScore")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("totalPerformance").place(2)).then(0.0)).`as`("totalPerformance")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("totalEmpScore").place(2)).then(0.0)).`as`("totalEmpScore")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("totalManagerScore").place(2)).then(0.0)).`as`("totalManagerScore")
        val sortStage = Aggregation.sort(Sort.by(Sort.Direction.DESC, "totalEmpScore"))
        val aggregation = Aggregation.newAggregation(groupStage, projectStage, sortStage)
        return reactiveMongoTemplate.aggregate(aggregation, constants.EMP_SCORE_COLLECTION_NAME, RoleScoreSummary::class.java)
    }

    fun getAverageScoresByGender(): Flux<GenderScoreSummary> {
        val groupStage = Aggregation.group("gender")
            .avg("performanace_score").`as`("avgPerformance")
            .sum("performanace_score").`as`("totalPerformance")
            .avg("emp_score").`as`("avgEmpScore")
            .sum("emp_score").`as`("totalEmpScore")
            .avg("manager_score").`as`("avgManagerScore")
            .sum("manager_score").`as`("totalManagerScore")

        val projectStage = Aggregation.project()
            .and("_id").`as`("gender")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("avgPerformance").place(2)).then(0.0)).`as`("avgPerformance")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("avgEmpScore").place(2)).then(0.0)).`as`("avgEmpScore")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("avgManagerScore").place(2)).then(0.0)).`as`("avgManagerScore")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("totalPerformance").place(2)).then(0.0)).`as`("totalPerformance")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("totalEmpScore").place(2)).then(0.0)).`as`("totalEmpScore")
            .and(ConditionalOperators.ifNull(ArithmeticOperators.Round.roundValueOf("totalManagerScore").place(2)).then(0.0)).`as`("totalManagerScore")
        val sortStage = Aggregation.sort(Sort.by(Sort.Direction.DESC, "totalEmpScore"))
        val aggregation = Aggregation.newAggregation(groupStage, projectStage, sortStage)
        return reactiveMongoTemplate.aggregate(aggregation, constants.EMP_SCORE_COLLECTION_NAME, GenderScoreSummary::class.java)
    }


    fun getDeptByJobWithRanges(): Flux<DeptJobRangeAgg> {

        fun lt(field: String, v: Number): AggregationExpression =
            ComparisonOperators.Lt.valueOf(field).lessThanValue(v)

        fun gte(field: String, v: Number): AggregationExpression =
            ComparisonOperators.Gte.valueOf(field).greaterThanEqualToValue(v)

        fun betweenIE(field: String, minInclusive: Number, maxExclusive: Number): AggregationExpression =
            BooleanOperators.And.and(
                gte(field, minInclusive),
                lt(field, maxExclusive)
            )

        val salaryRange = ConditionalOperators.switchCases(
            ConditionalOperators.Switch.CaseOperator.`when`(
                lt("salary", 5000)
            ).then("0–4.9k"),
            ConditionalOperators.Switch.CaseOperator.`when`(
                betweenIE("salary", 5000, 10000)
            ).then("5k–9.9k"),
            ConditionalOperators.Switch.CaseOperator.`when`(
                betweenIE("salary", 10000, 15000)
            ).then("10k–14.9k"),
            ConditionalOperators.Switch.CaseOperator.`when`(
                gte("salary", 15000)
            ).then("15k+")
        ).defaultTo("Unknown")

        val expRange = ConditionalOperators.switchCases(
            ConditionalOperators.Switch.CaseOperator.`when`(
                lt("years_of_experience", 3)
            ).then("0–2 yrs"),
            ConditionalOperators.Switch.CaseOperator.`when`(
                betweenIE("years_of_experience", 3, 6)
            ).then("3–5 yrs"),
            ConditionalOperators.Switch.CaseOperator.`when`(
                betweenIE("years_of_experience", 6, 11)
            ).then("6–10 yrs"),
            ConditionalOperators.Switch.CaseOperator.`when`(
                betweenIE("years_of_experience", 11, 16)
            ).then("11–15 yrs"),
            ConditionalOperators.Switch.CaseOperator.`when`(
                gte("years_of_experience", 16)
            ).then("16+ yrs")
        ).defaultTo("Unknown")

        val ageRange = ConditionalOperators.switchCases(
            ConditionalOperators.Switch.CaseOperator.`when`(
                lt("age", 25)
            ).then("18–24"),
            ConditionalOperators.Switch.CaseOperator.`when`(
                betweenIE("age", 25, 35)
            ).then("25–34"),
            ConditionalOperators.Switch.CaseOperator.`when`(
                betweenIE("age", 35, 45)
            ).then("35–44"),
            ConditionalOperators.Switch.CaseOperator.`when`(
                betweenIE("age", 45, 55)
            ).then("45–54"),
            ConditionalOperators.Switch.CaseOperator.`when`(
                gte("age", 55)
            ).then("55+")
        ).defaultTo("Unknown")

        val projectStage = project("department", "job_title", "salary", "years_of_experience", "age")
            .and(salaryRange).`as`("salaryRange")
            .and(expRange).`as`("expRange")
            .and(ageRange).`as`("ageRange")

        val groupStage = group("department", "job_title", "salaryRange", "expRange", "ageRange")
            .count().`as`("employeeCount")
            .avg("salary").`as`("avgSalary")
            .avg("years_of_experience").`as`("avgExperience")

        val formatStage = project()
            .and("_id.department").`as`("department")
            .and("_id.job_title").`as`("jobTitle")
            .and("_id.salaryRange").`as`("salaryRange")
            .and("_id.expRange").`as`("expRange")
            .and("_id.ageRange").`as`("ageRange")
            .and("employeeCount").`as`("employeeCount")
            .and(ArithmeticOperators.Round.roundValueOf("avgSalary").place(2)).`as`("avgSalary")
            .and(ArithmeticOperators.Round.roundValueOf("avgExperience").place(2)).`as`("avgExperience")

        val sortStage = sort(
            Sort.by(
                Sort.Order.asc("department"),
                Sort.Order.asc("jobTitle"),
                Sort.Order.asc("salaryRange"),
                Sort.Order.asc("expRange"),
                Sort.Order.asc("ageRange")
            )
        )

        val agg = Aggregation.newAggregation(projectStage, groupStage, formatStage, sortStage)

        return reactiveMongoTemplate.aggregate(agg, constants.EMP_INFO_COLLECTION_NAME, DeptJobRangeAgg::class.java)
    }

}




