package com.amarj.entity.client

sealed class SpentSummary

data class JobAndAgeSummary(
    val job: String,
    val age: Int?,
    val totalSpent: Double
) : SpentSummary()

data class JobSummary(
    val job: String?,
    val totalSpent: Double,
    val totalOrders: Double
) : SpentSummary()

data class HobbiesSummary(
    val hobbies: String?,
    val totalSpent: Double,
    val totalOrders: Double
) : SpentSummary()