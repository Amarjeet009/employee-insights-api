package com.amarj.controller

import com.amarj.entity.campaign.Campaign
import com.amarj.entity.campaign.CampaignAnalytics
import com.amarj.entity.campaign.CampaignTypeStats
import com.amarj.service.CampaignService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/v1/campaigns")
class CampaignController(private val campaignService: CampaignService) {

    @GetMapping("/campaignAnalytics")
    fun campaignAnalytics(): Flux<CampaignAnalytics> =
        campaignService.getCampaignAnalytics()

    @GetMapping("/statsByCampaignType")
    fun statsByCampaignType(): Flux<CampaignTypeStats> =
        campaignService.getStatsByCampaignType()
}