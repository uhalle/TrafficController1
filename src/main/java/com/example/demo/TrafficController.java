package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
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

	private boolean paused = false;

	@PostMapping("/runCycle")
    public String runCycle() {
    	if(paused) {
            return "Traffic cycle is paused!";
    	}
        northSouth.setState(LightState.GREEN);
        eastWest.setState(LightState.RED);
        validateNoConflict();
        
        northSouth.setState(LightState.YELLOW);
        eastWest.setState(LightState.RED);
        validateNoConflict();
        
        northSouth.setState(LightState.RED);
        eastWest.setState(LightState.GREEN);
        validateNoConflict();
        
        eastWest.setState(LightState.YELLOW);
        northSouth.setState(LightState.RED);
        validateNoConflict();
        return "Cycle executed successfully!";
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

    @PostMapping("/changeSequence")
    public String changeSequence(@RequestParam String sequence) {
        // Example: "NS_GREEN_EW_RED"
        if ("NS_GREEN_EW_RED".equalsIgnoreCase(sequence)) {
            northSouth.setState(LightState.GREEN);
            eastWest.setState(LightState.RED);
        } else if ("NS_RED_EW_GREEN".equalsIgnoreCase(sequence)) {
            northSouth.setState(LightState.RED);
            eastWest.setState(LightState.GREEN);
        }  else if ("NS_YELLO_EW_RED".equalsIgnoreCase(sequence)) {
            northSouth.setState(LightState.YELLOW);
            eastWest.setState(LightState.RED);
        } else if ("EW_YELLOW_NS_RED".equalsIgnoreCase(sequence)) {
            eastWest.setState(LightState.YELLOW);
            northSouth.setState(LightState.RED);
        } else {
            return "Invalid sequence!";
        }
        validateNoConflict();
        return "Sequence changed to: " + sequence;
    }
    
    @PostMapping("/validateConflictingDirections")
    private void validateNoConflict() {
        if (northSouth.getState() == LightState.GREEN && eastWest.getState() == LightState.GREEN) {
            throw new IllegalStateException("Conflict detected: Both directions cannot be GREEN simultaneously!");
        }
    }

    public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
}
