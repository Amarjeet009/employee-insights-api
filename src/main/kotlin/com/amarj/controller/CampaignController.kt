package com.amarj.controller

import com.amarj.entity.campaign.Campaign
import com.amarj.entity.campaign.CampaignAnalytics
import com.amarj.entity.campaign.CampaignStatsResponse
import com.amarj.entity.campaign.CampaignTypeStats
import com.amarj.service.CampaignService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/campaigns")
class CampaignController(private val campaignService: CampaignService) {

    @GetMapping("/campaignAnalytics")
    fun campaignAnalytics(): Flux<CampaignAnalytics> =
        campaignService.getCampaignAnalytics()

    @GetMapping("/getAllCampaignStats")
    fun getAllCampaignStats(): Mono<CampaignStatsResponse> {
        val typeStatsMono = campaignService.getStatsByCampaignType().collectList()
        val audienceStatsMono = campaignService.getStatsByCampaignTargetAudience().collectList()
        val channelStatsMono = campaignService.getStatsByCampaignChannel().collectList()

        return Mono.zip(typeStatsMono, audienceStatsMono, channelStatsMono)
            .map { tuple ->
                CampaignStatsResponse(
                    byType = tuple.t1,
                    byTargetAudience = tuple.t2,
                    byChannel = tuple.t3
                )
            }
    }
}