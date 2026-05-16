package com.smartbadge.adl.shared.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	@Value("${app.openapi.dev-url:http://localhost:8080}")
	private String devUrl;

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info().title("Staff Management API").version("1.0.0")
						.description("SmartBadge ADL Staff Management Service — Spring Modulith + MongoDB")
						.contact(new Contact().name("SmartBadge HRMS Team").email("hr@smartbadge.com"))
						.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
				.servers(List.of(new Server().url(devUrl).description("Development Server")));
	}
}
