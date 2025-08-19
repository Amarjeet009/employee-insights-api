package com.amarj.entity.info.analytics

data class RoleScoreSummary(
    val role: String,
    val avgPerformance: Double,
    val avgEmpScore: Double,
    val avgManagerScore: Double,
    val totalPerformance: Double,
    val totalEmpScore: Double,
    val totalManagerScore: Double
)

