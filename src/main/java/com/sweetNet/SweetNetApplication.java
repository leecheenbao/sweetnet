package com.sweetNet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.sweetNet")
@EnableScheduling
public class SweetNetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SweetNetApplication.class, args);
	}
	
}
