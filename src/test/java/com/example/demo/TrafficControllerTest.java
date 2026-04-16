package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Qualifier;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class TrafficControllerTest {

    @Autowired
    private TrafficController trafficController;

    @Autowired
    @Qualifier("NORTH_SOUTH")
    private TrafficLight northSouth;

    @Autowired
    @Qualifier("EAST_WEST")
    private TrafficLight eastWest;
    
    @Autowired
    private MockMvc mockMvc;
    

    @Test
    void testRunCycle() {
        trafficController.runCycle();

        // After cycle, expected states:
        // North-South should be RED
        // East-West should be YELLOW
        assertThat(northSouth.getState()).isEqualTo(TrafficLight.LightState.RED);
        assertThat(eastWest.getState()).isEqualTo(TrafficLight.LightState.YELLOW);
    }
    
    
    @Test
    void testRunCycle_With_Validation() {
        assertDoesNotThrow(() -> trafficController.runCycle());
    }
    
    @Test
    void testChangeSequence_NS_GREEN_EW_RED() throws Exception {
        mockMvc.perform(post("/api/traffic/changeSequence")
                        .param("sequence", "NS_GREEN_EW_RED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Sequence changed to: NS_GREEN_EW_RED"));

        assertEquals(LightState.GREEN.name(), trafficController.getNorthSouth().getState().name());
        assertEquals(LightState.RED.name(), trafficController.getEastWest().getState().name());
    }

    @Test
    void testChangeSequence_NS_RED_EW_GREEN() throws Exception {
        mockMvc.perform(post("/api/traffic/changeSequence")
                        .param("sequence", "NS_RED_EW_GREEN"))
                .andExpect(status().isOk())
                .andExpect(content().string("Sequence changed to: NS_RED_EW_GREEN"));

        assertEquals(LightState.RED.name(), trafficController.getNorthSouth().getState().name());
        assertEquals(LightState.GREEN.name(), trafficController.getEastWest().getState().name());
    }

    @Test
    void testChangeSequence_NS_YELLO_EW_RED() throws Exception {
        mockMvc.perform(post("/api/traffic/changeSequence")
                        .param("sequence", "NS_YELLO_EW_RED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Sequence changed to: NS_YELLO_EW_RED"));

        assertEquals(LightState.YELLOW.name(), trafficController.getNorthSouth().getState().name());
        assertEquals(LightState.RED.name(), trafficController.getEastWest().getState().name());
    }

    @Test
    void testChangeSequence_EW_YELLOW_NS_RED() throws Exception {
        mockMvc.perform(post("/api/traffic/changeSequence")
                        .param("sequence", "EW_YELLOW_NS_RED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Sequence changed to: EW_YELLOW_NS_RED"));

        assertEquals(LightState.RED.name(), trafficController.getNorthSouth().getState().name());
        assertEquals(LightState.YELLOW.name(), trafficController.getEastWest().getState().name());
    }

    @Test
    void testChangeSequence_Invalid() throws Exception {
        mockMvc.perform(post("/api/traffic/changeSequence")
                        .param("sequence", "INVALID"))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid sequence!"));
    }
    
    @Test
    void testRunCycle_When_Paused() {
    	trafficController.setPaused(true);
        String response = trafficController.runCycle();
        assertEquals("Traffic cycle is paused!", response);
    }
    
    @Test
    void testRunCycle_When_Resumed() {
    	trafficController.setPaused(false);
        String response = trafficController.runCycle();
        assertEquals("Cycle executed successfully!", response);
    }
}