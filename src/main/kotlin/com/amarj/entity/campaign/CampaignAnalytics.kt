package com.amarj.entity.campaign

data class CampaignAnalytics(
    val campaignName: String,
    val budget: Long,
    val durationDays: Long,
    val roiPercentage: Double,
    val conversionRatePercentage: Double,
    val revenuePerDollarSpent: Long,
    val channel: String,
    val targetAudience: String,
    val type: String
)