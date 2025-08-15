package com.amarj.service

import com.amarj.constants.ShareConstants
import com.amarj.entity.campaign.CampaignAnalytics
import com.amarj.entity.campaign.CampaignChannelStats
import com.amarj.entity.campaign.CampaignTargetAudienceStats
import com.amarj.entity.campaign.CampaignTypeStats
import com.amarj.repository.CampaignRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.temporal.ChronoUnit
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.group
import org.springframework.data.mongodb.core.aggregation.Aggregation.project
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators


@Service
class CampaignService(
    private val campaignRepository: CampaignRepository,
    private val mongoTemplate: ReactiveMongoTemplate,
    private val constants: ShareConstants
) {

    /**
     * Retrieves campaign analytics including budget, duration, ROI, conversion rate, revenue per dollar spent,
     * channel, target audience, and type.
     *
     * @return a Flux of CampaignAnalytics containing the analytics for each campaign.
     */

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
                    revenuePerDollarSpent = revenuePerDollarSpent,
                    channel = campaign.channel.uppercase(),
                    targetAudience = campaign.targetAudience,
                    type = campaign.type.uppercase()
                )
            }


    fun getStatsByCampaignType(): Flux<CampaignTypeStats> {
        val groupOperation = group("type")
            .sum("budget").`as`("totalBudget")
            .sum("roi" ).`as`("totalROI")
            .sum("conversion_rate").`as`("totalConversionRate")
            .sum("revenue").`as`("totalRevenue")

        val projectOperation = project()
            .and("totalBudget").`as`("totalBudget")
            .and(
                ArithmeticOperators.Round.roundValueOf("totalROI")
                    .place(2)
            ).`as`("totalROI")
            .and(
                ArithmeticOperators.Round.roundValueOf(
                    ArithmeticOperators.Multiply.valueOf("totalConversionRate")
                        .multiplyBy(100)
                ).place(2)
            ).`as`("totalConversionRate")
             .and("totalRevenue").`as`("totalRevenue")
            .and("_id").`as`("type")
            .andExclude("_id") // map _id to type and exclude it

        val aggregation = Aggregation.newAggregation(groupOperation, projectOperation)

        return mongoTemplate.aggregate(aggregation, constants.COMPAIGNS_INFO_COLLECTION_NAME, CampaignTypeStats::class.java)
    }

    fun getStatsByCampaignTargetAudience(): Flux<CampaignTargetAudienceStats> {
        val groupOperation = group("target_audience")
            .sum("budget").`as`("totalBudget")
            .sum("roi").`as`("totalROI")
            .sum("conversion_rate").`as`("totalConversionRate")
            .sum("revenue").`as`("totalRevenue")

        val projectOperation = project()
            .and("_id").`as`("targetAudience") // map _id to targetAudience
            .and("totalBudget").`as`("totalBudget")
            .and(
                ArithmeticOperators.Round.roundValueOf("totalROI")
                    .place(2)
            ).`as`("totalROI")
            .and(
                ArithmeticOperators.Round.roundValueOf(
                    ArithmeticOperators.Multiply.valueOf("totalConversionRate")
                        .multiplyBy(100)
                ).place(2)
            ).`as`("totalConversionRate")
            .and("totalRevenue").`as`("totalRevenue")
            .andExclude("_id") // optional, since it's already mapped

        val aggregation = Aggregation.newAggregation(groupOperation, projectOperation)

        return mongoTemplate.aggregate(
            aggregation,
            constants.COMPAIGNS_INFO_COLLECTION_NAME,
            CampaignTargetAudienceStats::class.java
        )
    }

    fun getStatsByCampaignChannel(): Flux<CampaignChannelStats> {
        val groupOperation = group("channel")
            .sum("budget").`as`("totalBudget")
            .sum("roi").`as`("totalROI")
            .sum("conversion_rate").`as`("totalConversionRate")
            .sum("revenue").`as`("totalRevenue")

        val projectOperation = project()
            .and("_id").`as`("channel") // map _id to channel
            .and("totalBudget").`as`("totalBudget")
            .and(
                ArithmeticOperators.Round.roundValueOf("totalROI")
                    .place(2)
            ).`as`("totalROI")
            .and(
                ArithmeticOperators.Round.roundValueOf(
                    ArithmeticOperators.Multiply.valueOf("totalConversionRate")
                        .multiplyBy(100)
                ).place(2)
            ).`as`("totalConversionRate")
            .and("totalRevenue").`as`("totalRevenue")
            .andExclude("_id") // optional, since it's already mapped

        val aggregation = Aggregation.newAggregation(groupOperation, projectOperation)

        return mongoTemplate.aggregate(
            aggregation,
            constants.COMPAIGNS_INFO_COLLECTION_NAME,
            CampaignChannelStats::class.java
        )
    }
}