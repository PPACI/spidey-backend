package io.spidey.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class SpideyConfiguration {

    @Bean
    fun corsConfig(): WebMvcConfigurer {
        class WebConfig : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
            }

        }
        return WebConfig()
    }
}