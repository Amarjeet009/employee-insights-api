package com.amarj.repository

import com.amarj.entity.client.ClientInfo
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.awt.print.Pageable

@Repository
interface ClientInfoRepository : ReactiveMongoRepository<ClientInfo, String>{
    fun findAllBy(pageable: Pageable): Flux<ClientInfo>
}

