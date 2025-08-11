package com.amarj.entity.client

data class SpentSummaryResponse(
    val groupBy: String,
    val data: List<Any>
)