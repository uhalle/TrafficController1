package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentStatus {
	private Map<String, String> currentStateForEachDirection = new HashMap<>();
	private Map<String, List<String>> timeHistoryOfEachDirection = new HashMap<>();
	boolean status;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Map<String, String> getCurrentStateForEachDirection() {
		return currentStateForEachDirection;
	}

	public void setCurrentStateForEachDirection(Map<String, String> currentStateForEachDirection) {
		this.currentStateForEachDirection = currentStateForEachDirection;
	}

	public Map<String, List<String>> getTimeHistoryOfEachDirection() {
		return timeHistoryOfEachDirection;
	}

	public void setTimeHitoryOfEachDirection(Map<String, List<String>> timeHitoryOfEachDirection) {
		this.timeHistoryOfEachDirection = timeHitoryOfEachDirection;
	}
}
