package com.amarj.repository

import com.amarj.entity.info.EmployeeInfo
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeInfoRepository: ReactiveMongoRepository<EmployeeInfo, String>