package com.groom.swipo.global;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
public class ProfileController {

	private final Environment env;

	public ProfileController(Environment env) {
		this.env = env;
	}

	@GetMapping("/profile")
	public String profile() {
		List<String> profiles = Arrays.asList(env.getActiveProfiles());
		List<String> realProfiles = Arrays.asList("real1", "real2");
		String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);

		return profiles.stream()
			.filter(realProfiles::contains)
			.findAny()
			.orElse(defaultProfile);
	}
}
