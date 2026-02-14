package com.br.desafio.ras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RasApplication {

	public static void main(String[] args) {
		SpringApplication.run(RasApplication.class, args);
	}

}
