package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// Load from .env
		Dotenv dotenv = Dotenv.load();

		String dbPassword = dotenv.get("DB_PASSWORD");
		String apiKey = dotenv.get("FIN_API");

		// Optional: Check if values exist
		if (dbPassword == null || apiKey == null) {
			System.err.println("Missing environment variables! Check your .env file.");
			System.exit(1);
		}

		// Load only dynamic properties (others stay in application.properties)
		Map<String, Object> props = new HashMap<>();
		props.put("spring.datasource.password", dbPassword);
		props.put("finnhub.api.key", apiKey); // Use this custom key in your services

		new SpringApplicationBuilder(DemoApplication.class)
				.properties(props)
				.run(args);
	}
}
