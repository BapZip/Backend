package com.example.BapZip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BapZipApplication {

	public static void main(String[] args) {
		SpringApplication.run(BapZipApplication.class, args);
	}

}
