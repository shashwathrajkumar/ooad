package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

	public static void main(String[] args) {
		// Load from .env
		Dotenv dotenv = Dotenv.load();

		String dbPassword = dotenv.get("DB_PASSWORD");

		// Optional: Check if values exist
		if (dbPassword == null) {
			System.err.println("Missing environment variables! Check your .env file.");
			System.exit(1);
		}

		// Load only dynamic properties (others stay in application.properties)
		Map<String, Object> props = new HashMap<>();
		props.put("spring.datasource.password", dbPassword);

		new SpringApplicationBuilder(DemoApplication.class)
				.properties(props)
				.run(args);
	}
}
