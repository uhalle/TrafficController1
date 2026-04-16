package com.example.demo;

public enum LightState {
	RED, GREEN, YELLOW;

	public LightState next() {
		switch (this) {
		case RED:
			return GREEN;
		case GREEN:
			return YELLOW;
		case YELLOW:
			return RED;
		default:
			throw new IllegalStateException();
		}
	}
}
