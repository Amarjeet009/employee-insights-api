package com.amarj.entity.campaign

data class CampaignStatsResponse(
    val byType: List<CampaignTypeStats>,
    val byTargetAudience: List<CampaignTargetAudienceStats>,
    val byChannel: List<CampaignChannelStats>
)