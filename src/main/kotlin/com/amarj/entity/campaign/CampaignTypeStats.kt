package com.amarj.entity.campaign

data class CampaignTypeStats(
    val type: String,
    val totalBudget: Long,
    val totalROI: Long,
    val totalConversionRate: Long,
    val totalRevenue: Long
)