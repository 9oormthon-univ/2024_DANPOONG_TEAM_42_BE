package com.groom.swipo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SwipoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwipoApplication.class, args);
	}

}
