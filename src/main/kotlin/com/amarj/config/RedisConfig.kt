package com.amarj.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.data.redis.core.ReactiveRedisTemplate

@Configuration
class RedisConfig {

    @Bean
    fun redisSerializationContext(): RedisSerializationContext<String, String> {
        return RedisSerializationContext
            .newSerializationContext<String, String>(StringRedisSerializer())
            .value(StringRedisSerializer())
            .build()
    }

    @Bean
    fun reactiveRedisTemplate(
        factory: ReactiveRedisConnectionFactory,
        context: RedisSerializationContext<String, String>
    ): ReactiveRedisTemplate<String, String> {
        return ReactiveRedisTemplate(factory, context)
    }
}
