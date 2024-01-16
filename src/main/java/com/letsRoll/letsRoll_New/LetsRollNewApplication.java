package com.letsRoll.letsRoll_New;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LetsRollNewApplication {

	public static void main(String[] args) {
		SpringApplication.run(LetsRollNewApplication.class, args);
	}
}
