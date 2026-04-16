package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import com.example.demo.TrafficLight.LightState;;

@RestController
@RequestMapping("/api/traffic/")
public class TrafficController {

	private TrafficLight northSouth;
	private TrafficLight eastWest;
	private boolean paused = false;
	static final String NORTH_SOUTH = "North-South";
    static final String EAST_WEST = "East-West";
	static final String NS_GREEN_EW_RED = "NS_GREEN_EW_RED";
	static final String NS_RED_EW_GREEN = "NS_RED_EW_GREEN";
	static final String NS_YELLOW_EW_RED = "NS_YELLO_EW_RED";
	static final String EW_YELLOW_NS_RED = "EW_YELLOW_NS_RED";

	@PostMapping("/runCycle")
	public String runCycle() {
		if (paused) {
			return "Traffic cycle is paused!";
		}
		getNorthSouth().setState(LightState.GREEN);
		getEastWest().setState(LightState.RED);
		validateNoConflict();

		getNorthSouth().setState(LightState.YELLOW);
		getEastWest().setState(LightState.RED);
		validateNoConflict();

		getNorthSouth().setState(LightState.RED);
		getEastWest().setState(LightState.GREEN);
		validateNoConflict();

		getEastWest().setState(LightState.YELLOW);
		getNorthSouth().setState(LightState.RED);
		validateNoConflict();
		return "Cycle executed successfully!";
	}

	@PostMapping("/changeSequence")
	public String changeSequence(@RequestParam String sequence) {
		// Example: "NS_GREEN_EW_RED"
		if (NS_GREEN_EW_RED.equalsIgnoreCase(sequence)) {
			getNorthSouth().setState(LightState.GREEN);
			getEastWest().setState(LightState.RED);
		} else if (NS_RED_EW_GREEN.equalsIgnoreCase(sequence)) {
			getNorthSouth().setState(LightState.RED);
			getEastWest().setState(LightState.GREEN);
		} else if (NS_YELLOW_EW_RED.equalsIgnoreCase(sequence)) {
			getNorthSouth().setState(LightState.YELLOW);
			getEastWest().setState(LightState.RED);
		} else if (EW_YELLOW_NS_RED.equalsIgnoreCase(sequence)) {
			getEastWest().setState(LightState.YELLOW);
			getNorthSouth().setState(LightState.RED);
		} else {
			return "Invalid sequence!";
		}
		validateNoConflict();
		return "Sequence changed to: " + sequence;
	}

	@PostMapping("/validateConflictingDirections")
	private void validateNoConflict() {
		if (getNorthSouth().getState() == LightState.GREEN && getEastWest().getState() == LightState.GREEN) {
			throw new IllegalStateException("Conflict detected: Both directions cannot be GREEN simultaneously!");
		}
	}

	@PostMapping("/pause")
	public String pause() {
		paused = true;
		return "Traffic cycle paused.";
	}

	@PostMapping("/resume")
	public String resume() {
		paused = false;
		return "Traffic cycle resumed.";
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	@GetMapping("/currentStateAndTimeHistoryOfEachDirection")
	public CurrentStatus getCurrentStateOfEachDirection() {
		CurrentStatus currentStatus = new CurrentStatus();
		currentStatus.setStatus(isPaused());
		currentStatus.getCurrentStateForEachDirection().put(NORTH_SOUTH, getNorthSouth().getState().name());
		currentStatus.getCurrentStateForEachDirection().put(EAST_WEST, getEastWest().getState().name());
		currentStatus.getTimeHistoryOfEachDirection().put(NORTH_SOUTH, getNorthSouth().getHistory());
		currentStatus.getTimeHistoryOfEachDirection().put(EAST_WEST, getNorthSouth().getHistory());
		return currentStatus;
	}

	public TrafficLight getNorthSouth() {
		return northSouth;
	}

	@Autowired
	@Qualifier("NORTH_SOUTH")
	public void setNorthSouth(TrafficLight northSouth) {
		this.northSouth = northSouth;
	}

	public TrafficLight getEastWest() {
		return eastWest;
	}

	@Autowired
	@Qualifier("EAST_WEST")
	public void setEastWest(TrafficLight eastWest) {
		this.eastWest = eastWest;
	}

}
