package com.example.demo;

import org.springframework.stereotype.Component;

public enum DIRECTION {
	    NORTH_SOUTH("north-south"), EAST_WEST("east-west");

	   String direction;
	   
	   DIRECTION(String direction) {
		   this.direction = direction;
		}
}
