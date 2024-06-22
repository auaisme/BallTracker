package com.aua.BallTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BallTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BallTrackerApplication.class, args);
	}

}
