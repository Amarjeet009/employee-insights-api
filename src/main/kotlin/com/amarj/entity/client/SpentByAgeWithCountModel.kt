package com.amarj.entity.client

data class SpentByAgeWithCountModel(
        val age: Int,         // or String if your DB stores it that way
        val totalSpent: Double,
        val employeeCount: Int
    )