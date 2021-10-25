package com.ingendevelopment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.ingendevelopment.model.persistence.repositories")
@EntityScan("com.ingendevelopment.model.persistence")
@SpringBootApplication
public class SareetaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SareetaApplication.class, args);
	}

}