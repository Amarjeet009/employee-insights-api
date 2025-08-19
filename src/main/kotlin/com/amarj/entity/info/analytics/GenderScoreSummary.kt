package com.amarj.entity.info.analytics

data class GenderScoreSummary(
    val gender: String,
    val avgPerformance: Double,
    val avgEmpScore: Double,
    val avgManagerScore: Double,
    val totalPerformance: Double,
    val totalEmpScore: Double,
    val totalManagerScore: Double
)