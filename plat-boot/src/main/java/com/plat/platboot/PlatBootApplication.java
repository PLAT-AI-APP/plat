package com.plat.platboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
	scanBasePackages = {
		"com.plat.platboot",
		"com.plat.platdata"
	}
)
public class PlatBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatBootApplication.class, args);
	}

}
