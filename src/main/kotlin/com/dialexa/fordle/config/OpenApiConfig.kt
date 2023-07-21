package com.dialexa.fordle.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun fordleOpenAPI(): OpenAPI {
        return OpenAPI()
            .servers(getServers())
            .components(Components())
            .info(getApiInfo())
    }

    private fun getApiInfo(): Info {
        return Info()
                .title("Fordle")
                .description("Fake wordle for demo purposes")
    }

    private fun getServers(): List<Server> {
        val localServer = Server().description("local").url("http://localhost:8080")
        return listOf(localServer)
    }
}