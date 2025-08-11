package com.amarj.repository

import com.amarj.entity.campaign.Campaign
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CampaignRepository: ReactiveMongoRepository<Campaign, ObjectId> {
}