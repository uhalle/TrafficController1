package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrafficLightConfig {

	@Bean(name = "NORTH_SOUTH")
	public TrafficLight northSouthLight() {
		return new TrafficLight(DIRECTION.NORTH_SOUTH);
	}

	@Bean(name = "EAST_WEST")
	public TrafficLight eastWestLight() {
		return new TrafficLight(DIRECTION.EAST_WEST);
	}
}
