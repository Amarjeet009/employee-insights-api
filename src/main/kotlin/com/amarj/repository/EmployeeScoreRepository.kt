package com.amarj.repository

import com.amarj.entity.info.analytics.EmployeeScore
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface EmployeeScoreRepository: ReactiveMongoRepository<EmployeeScore, String>