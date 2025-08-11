package com.amarj.service

import com.amarj.constants.ShareConstants
import com.amarj.entity.client.ClientInfo
import com.amarj.entity.client.SpentAndOrderByHobbiesModal
import com.amarj.entity.client.SpentAndOrderByJobModal
import com.amarj.entity.client.SpentByJobAndAgeModal
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ClientInfoService(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val constants: ShareConstants

) {


    fun getClientDetails(page: Int, size: Int): Flux<ClientInfo> {
        val skip = page * size
        val query = Query()
            .with(Sort.by(Sort.Direction.DESC, "spent"))
            .skip(skip.toLong())
            .limit(size)

        return mongoTemplate.find(query, ClientInfo::class.java)
    }

    fun countClient(): Mono<Long> {
        return mongoTemplate.count(Query(), ClientInfo::class.java)
    }

    fun getSpentByJobAndAge(): Flux<SpentByJobAndAgeModal> {
        val groupStage = Aggregation.group("job", "age")
            .sum("spent").`as`("totalSpent")

        val projectStage = Aggregation.project()
            .and("_id.job").`as`("job")
            .and("_id.age").`as`("age")
            .and("totalSpent").`as`("totalSpent")

        val sortStage = Aggregation.sort(Sort.by(Sort.Direction.DESC, "totalSpent"))

        val aggregation = Aggregation.newAggregation(groupStage, projectStage, sortStage)

        return mongoTemplate.aggregate(aggregation, constants.CLIENT_COLLECTION_NAME, SpentByJobAndAgeModal::class.java)
    }

    fun getSpentAndOrderByJob(): Flux<SpentAndOrderByJobModal> {
        val groupStage = Aggregation.group("job")
            .sum("spent").`as`("totalSpent")
            .sum("orders"). `as`("totalOrders")

        val projectStage = Aggregation.project()
            .and("_id").`as`("job")
            .and("totalSpent").`as`("totalSpent")
            .and("totalOrders").`as`("totalOrders")

        val sortStage = Aggregation.sort(Sort.by(Sort.Direction.DESC, "totalSpent"))

        val aggregation = Aggregation.newAggregation(groupStage, projectStage, sortStage)

        return mongoTemplate.aggregate(aggregation, constants.CLIENT_COLLECTION_NAME, SpentAndOrderByJobModal::class.java)
    }

    fun getSpentAndOrderByHobbies(): Flux<SpentAndOrderByHobbiesModal> {
        val groupStage = Aggregation.group("hobbies")
            .sum("spent").`as`("totalSpent")
            .sum("orders"). `as`("totalOrders")

        val projectStage = Aggregation.project()
            .and("_id").`as`("hobbies")
            .and("totalSpent").`as`("totalSpent")
            .and("totalOrders").`as`("totalOrders")

        val sortStage = Aggregation.sort(Sort.by(Sort.Direction.DESC, "totalSpent"))

        val aggregation = Aggregation.newAggregation(groupStage, projectStage, sortStage)

        return mongoTemplate.aggregate(aggregation, constants.CLIENT_COLLECTION_NAME, SpentAndOrderByHobbiesModal::class.java)
    }
}
