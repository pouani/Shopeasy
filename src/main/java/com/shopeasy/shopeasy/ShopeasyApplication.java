package com.shopeasy.shopeasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling // Active le scheduling pour le nettoyage automatique des tokens
public class ShopeasyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopeasyApplication.class, args);
	}

}
