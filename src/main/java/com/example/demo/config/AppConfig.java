package com.example.demo.config;
import io.github.cdimascio.dotenv.Dotenv;

public class AppConfig {
    public static Dotenv dotenv = Dotenv.load();

    public static String getDbUsername() {
        return dotenv.get("DB_USERNAME");
    }

    public static String getDbPassword() {
        return dotenv.get("DB_PASSWORD");
    }
}
