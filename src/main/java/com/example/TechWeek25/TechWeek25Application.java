package com.example.TechWeek25;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TechWeek25Application {

	public static void main(String[] args) {
		SpringApplication.run(TechWeek25Application.class, args);
	}

}
