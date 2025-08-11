package com.amarj.service

import com.amarj.constants.ShareConstants
import com.amarj.entity.campaign.CampaignAnalytics
import com.amarj.entity.campaign.CampaignTypeStats
import com.amarj.repository.CampaignRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.temporal.ChronoUnit
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.group
import org.springframework.data.mongodb.core.aggregation.Aggregation.project


@Service
class CampaignService(
    private val campaignRepository: CampaignRepository,
    private val mongoTemplate: ReactiveMongoTemplate,
    private val constants: ShareConstants
) {

    fun getCampaignAnalytics(): Flux<CampaignAnalytics> =
        campaignRepository.findAll()
            .map { campaign ->
                val durationDays = ChronoUnit.DAYS.between(campaign.startDate, campaign.endDate)
                val roiPercentage = campaign.roi * 100
                val conversionRatePercentage = campaign.conversionRate * 100
                val revenuePerDollarSpent = if (campaign.budget != 0L) campaign.revenue / campaign.budget else 0
                CampaignAnalytics(
                    campaignName = campaign.campaignName,
                    budget = campaign.budget,
                    durationDays = durationDays,
                    roiPercentage = roiPercentage,
                    conversionRatePercentage = conversionRatePercentage,
                    revenuePerDollarSpent = revenuePerDollarSpent
                )
            }


    fun getStatsByCampaignType(): Flux<CampaignTypeStats> {
        val groupOperation = group("type")
            .sum("budget").`as`("totalBudget")
            .sum("roi").`as`("totalROI")
            .sum("conversionRate").`as`("totalConversionRate")
            .sum("revenue").`as`("totalRevenue")

        val projectOperation = project()
            .and("totalBudget").`as`("totalBudget")
            .and("totalROI").`as`("totalROI")
            .and("totalConversionRate").`as`("totalConversionRate")
            .and("totalRevenue").`as`("totalRevenue")
            .and("_id").`as`("type")
            .andExclude("_id")

        val aggregation = Aggregation.newAggregation(groupOperation, projectOperation)

        return mongoTemplate.aggregate(aggregation, constants.COMPAIGNS_INFO_COLLECTION_NAME, CampaignTypeStats::class.java)
    }
}