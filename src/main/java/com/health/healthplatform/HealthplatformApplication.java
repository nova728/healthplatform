package com.health.healthplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@SpringBootApplication
public class HealthplatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthplatformApplication.class, args);
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
		return jacksonObjectMapperBuilder ->
				jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("GMT+8"))
						.dateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
	}
}
