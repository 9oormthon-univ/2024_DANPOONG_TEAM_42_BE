package com.groom.swipo.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siot.IamportRestClient.IamportClient;

@Configuration
public class PortOneConfig {

	@Value("${imp.api.key}")
	private String apiKey;

	@Value("${imp.api.secret-key}")
	private String secretKey;

	@Bean
	public IamportClient iamportClient() {
		return new IamportClient(apiKey, secretKey);
	}
}
