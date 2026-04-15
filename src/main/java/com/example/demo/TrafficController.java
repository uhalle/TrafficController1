package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import com.example.demo.TrafficLight.LightState;;

@RestController
@RequestMapping("/api")
public class TrafficController {
	
	@Autowired
    @Qualifier("NORTH_SOUTH")
    private TrafficLight northSouth;

    @Autowired
    @Qualifier("EAST_WEST")
    private TrafficLight eastWest;

    public void runCycle() {
        northSouth.setState(LightState.GREEN);
        eastWest.setState(LightState.RED);

        northSouth.setState(LightState.YELLOW);
        eastWest.setState(LightState.RED);

        northSouth.setState(LightState.RED);
        eastWest.setState(LightState.GREEN);

        northSouth.setState(LightState.RED);
        eastWest.setState(LightState.YELLOW);
    }
	
	public void controlTraffic() {
		
	}

}
