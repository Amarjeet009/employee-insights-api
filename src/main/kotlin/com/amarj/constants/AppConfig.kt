package com.amarj.constants

import com.amarj.constants.ShareConstants
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun constants(): ShareConstants = ShareConstants
}