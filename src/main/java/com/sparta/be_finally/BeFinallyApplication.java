package com.sparta.be_finally;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling //스케줄링 기능을 enable 함.
public class BeFinallyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeFinallyApplication.class, args);
	}

}

