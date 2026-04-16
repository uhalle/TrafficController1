package com.example.demo;

public enum DIRECTION {
    NORTH_SOUTH("north-south"), EAST_WEST("east-west");

   String direction;
   
   DIRECTION(String direction) {
	   this.direction = direction;
	}
}
