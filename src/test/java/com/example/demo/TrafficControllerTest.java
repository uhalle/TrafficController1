package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Qualifier;

@SpringBootTest
class TrafficControllerTest {

    @Autowired
    private TrafficController trafficController;

    @Autowired
    @Qualifier("NORTH_SOUTH")
    private TrafficLight northSouth;

    @Autowired
    @Qualifier("EAST_WEST")
    private TrafficLight eastWest;

    @Test
    void testRunCycle() {
        // Run the cycle
        trafficController.runCycle();

        // After cycle, expected states:
        // North-South should be RED
        // East-West should be YELLOW
        assertThat(northSouth.getState()).isEqualTo(TrafficLight.LightState.RED);
        assertThat(eastWest.getState()).isEqualTo(TrafficLight.LightState.YELLOW);
    }
}