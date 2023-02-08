package com.sparta.be_finally;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling //스케줄링 기능을 enable 함.
public class BeFinallyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeFinallyApplication.class, args);
	}

}

