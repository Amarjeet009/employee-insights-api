package com.amarj.entity.campaign

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDate

@Document(collection = "marketing_campaigns")
data class Campaign(
    @Id
    val _id: ObjectId? = null,
    @Field("campaign_name")
    val campaignName: String,
    @Field("start_date")
    val startDate: LocalDate,
    @Field("end_date")
    val endDate: LocalDate,
    @Field("budget")
    val budget: Long,
    @Field("roi")
    val roi: Double,
    @Field("type")
    val type: String,
    @Field("target_audience")
    val targetAudience: String,
    @Field("channel")
    val channel: String,
    @Field("conversion_rate")
    val conversionRate: Double,
    @Field("revenue")
    val revenue: Long
)