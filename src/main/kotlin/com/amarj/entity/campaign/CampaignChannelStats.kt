package com.amarj.entity.campaign

data class CampaignChannelStats(
    val channel: String,
    val totalBudget: Long,
    val totalROI: Long,
    val totalConversionRate: Double,
    val totalRevenue: Long
)