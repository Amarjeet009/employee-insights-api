package com.amarj.entity.client

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field


@Document(collection = "client_insights")
data class ClientInfo(
    @Id
    val id: String? = null,
    @Field("first_name")
    val firstName: String,
    @Field("last_name")
    val lastName: String,
    @Field("email")
    val email: String,
    @Field("phone")
    val phone: String,
    @Field("address")
    val address: String,
    @Field("gender")
    val gender: String,
    @Field("age")
    @field:Min(18, message = "age must be at least 18")
    @field:Max(65, message = "age must not exceed 65")
    val age: Int,
    @Field("registered")
    val registered: String,
    @Field("orders")
    val orders: Int,
    @Field("spent")
    val spent: Double,
    @Field("job")
    val job: String,
    @Field("hobbies")
    val hobbies: String,
    @Field("is_married")
    val isMarried: Boolean

)
