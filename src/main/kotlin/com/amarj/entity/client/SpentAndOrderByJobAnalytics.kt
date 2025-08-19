package com.amarj.entity.client

data class SpentAndOrderByJobAnalytics(
        val job: String,
        val totalSpent: Double,
        val totalOrders: Int,
        val employeeCount: Int,
        val avgSpentPerOrder: Double
    )