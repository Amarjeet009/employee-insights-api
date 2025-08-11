package com.amarj.repository

import com.amarj.constants.ShareConstants
import com.amarj.entity.info.DepartmentAnalytics
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.stereotype.Repository
import com.amarj.entity.info.DepartmentSalary
import reactor.core.publisher.Flux

data class DepartmentSalary(val department: String, val salary: Double)

@Repository
class EmployeeCustomRepository(private val mongoTemplate: ReactiveMongoTemplate,
    private val shareConstants: ShareConstants) {

    fun getHighestSalaryPerDepartment(): Flux<DepartmentSalary> {
        val aggregation = Aggregation.newAggregation(
            Aggregation.group("department")
                .max("salary").`as`("highestSalary"),
            Aggregation.project()
                .and("_id").`as`("department")
                .and("highestSalary").`as`("highestSalary")
        )

        return mongoTemplate.aggregate(aggregation, shareConstants.EMP_INFO_COLLECTION_NAME, DepartmentSalary::class.java)
    }

    fun getDepartmentAnalytics(): Flux<DepartmentAnalytics> {
        val aggregation = Aggregation.newAggregation(
            Aggregation.group("department")
                .max("salary").`as`("highestSalary")
                .avg("salary").`as`("averageSalary")
                .count().`as`("employeeCount"),
            Aggregation.project()
                .and("_id").`as`("department")
                .and("highestSalary").`as`("highestSalary")
                .and("averageSalary").`as`("averageSalary")
                .and("employeeCount").`as`("employeeCount")
        )

        return mongoTemplate.aggregate(aggregation, shareConstants.EMP_INFO_COLLECTION_NAME, DepartmentAnalytics::class.java)
    }
}