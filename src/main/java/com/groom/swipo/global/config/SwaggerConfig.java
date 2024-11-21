package com.groom.swipo.global.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Value("${app.local-url}")
	private String localUrl;

	@Value("${app.prod-url}")
	private String prodUrl;

	private Info apiInfo() {
		return new Info()
			.version("v1.0.0")
			.title("Swipo API")
			.description("Swipo API 명세서");
	}

	@Bean
	public OpenAPI openAPI() {
		String authHeader = "Authorization";

		Server localServer = new Server();
		localServer.description("Local Server")
			.url(localUrl);

		Server prodServer = new Server();
		prodServer.description("Production Server")
			.url(prodUrl);

		return new OpenAPI()
			.info(apiInfo())
			.addSecurityItem(new SecurityRequirement().addList(authHeader))
			.components(new Components()
				.addSecuritySchemes(authHeader, new SecurityScheme()
					.name(authHeader)
					.type(SecurityScheme.Type.HTTP)
					.scheme("Bearer")
					.bearerFormat("accessToken")))
			.servers(Arrays.asList(localServer, prodServer));
	}
}
