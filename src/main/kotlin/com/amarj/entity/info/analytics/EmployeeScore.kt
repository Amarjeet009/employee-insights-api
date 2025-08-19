package com.amarj.entity.info.analytics

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("emp_scores")
data class EmployeeScore(
    @Id val id: String? = null,
    @Field("first_name")
    val firstName: String,
    @Field("last_name")
    val lastName: String,
    @Field("email")
    val email: String,
    @Field("phone")
    val gender: String,
    @Field("full_time_job")
    val fullTimeJob: Boolean,
    @Field("absence_days")
    val absenceDays: Int,
    @Field("extracurricular_activities")
    val extracurricularActivities: Boolean,
    @Field("weekly_hours")
    val weeklyHours: Int,
    @Field("role")
    val role: String,
    @Field("client_score")
    val clientScore: Int,
    @Field("manager_score")
    val managerScore: Int,
    @Field("emp_score")
    val empScore: Int,
    @Field("company_score")
    val companyScore: Int,
    @Field("performanace_score")
    val performanaceScore: Int,
    @Field("insights_score")
    val insightsScore: Int,
    @Field("extra_activity_score")
    val extraActivityScore: Int
)