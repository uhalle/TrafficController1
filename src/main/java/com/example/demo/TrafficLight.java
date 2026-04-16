package com.example.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficLight {

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

	private LightState state = LightState.RED;
	private String direction;
	private List<String> history = new ArrayList<>();
	private final Lock lock = new ReentrantLock();

	public TrafficLight(DIRECTION direction) {
		this.direction = direction.direction;
	}

	public LightState getState() {
		return state;
	}

	public void setState(LightState lightState) {
		lock.lock();
		try {
			history.add(
					"Status Changed to" + state.name() + "at" + LocalDateTime.now() + "for the direction" + direction);
			this.state = lightState;
		} finally {
			lock.unlock();
		}
	}

	public String getDirection() {
		return direction;
	}

	public List<String> getHistory() {
		return history;
	}

}
