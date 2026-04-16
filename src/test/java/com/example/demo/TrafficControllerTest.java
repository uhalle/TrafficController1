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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static com.example.demo.TrafficController.EAST_WEST;
import static com.example.demo.TrafficController.NORTH_SOUTH;
import static com.example.demo.TrafficController.NS_RED_EW_GREEN;
import static com.example.demo.TrafficController.NS_GREEN_EW_RED;
import static com.example.demo.TrafficController.NS_YELLOW_EW_RED;
import static com.example.demo.TrafficController.EW_YELLOW_NS_RED;

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
		mockMvc.perform(post("/api/traffic/changeSequence").param("sequence", NS_GREEN_EW_RED))
				.andExpect(status().isOk()).andExpect(content().string("Sequence changed to: NS_GREEN_EW_RED"));

		assertEquals(LightState.GREEN.name(), trafficController.getNorthSouth().getState().name());
		assertEquals(LightState.RED.name(), trafficController.getEastWest().getState().name());
	}

	@Test
	void testChangeSequence_NS_RED_EW_GREEN() throws Exception {
		mockMvc.perform(post("/api/traffic/changeSequence").param("sequence", NS_RED_EW_GREEN))
				.andExpect(status().isOk()).andExpect(content().string("Sequence changed to: NS_RED_EW_GREEN"));

		assertEquals(LightState.RED.name(), trafficController.getNorthSouth().getState().name());
		assertEquals(LightState.GREEN.name(), trafficController.getEastWest().getState().name());
	}

	@Test
	void testChangeSequence_NS_YELLO_EW_RED() throws Exception {
		mockMvc.perform(post("/api/traffic/changeSequence").param("sequence", NS_YELLOW_EW_RED))
				.andExpect(status().isOk()).andExpect(content().string("Sequence changed to: NS_YELLO_EW_RED"));

		assertEquals(LightState.YELLOW.name(), trafficController.getNorthSouth().getState().name());
		assertEquals(LightState.RED.name(), trafficController.getEastWest().getState().name());
	}

	@Test
	void testChangeSequence_EW_YELLOW_NS_RED() throws Exception {
		mockMvc.perform(post("/api/traffic/changeSequence").param("sequence", EW_YELLOW_NS_RED))
				.andExpect(status().isOk()).andExpect(content().string("Sequence changed to: EW_YELLOW_NS_RED"));

		assertEquals(LightState.RED.name(), trafficController.getNorthSouth().getState().name());
		assertEquals(LightState.YELLOW.name(), trafficController.getEastWest().getState().name());
	}

	@Test
	void testChangeSequence_Invalid() throws Exception {
		mockMvc.perform(post("/api/traffic/changeSequence").param("sequence", "INVALID")).andExpect(status().isOk())
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

	@Test
	void testCurrentStateAndTimeHistory_Default() throws Exception {
		mockMvc.perform(post("/api/traffic/changeSequence").param("sequence", NS_GREEN_EW_RED))
				.andExpect(status().isOk());

		trafficController.getEastWest().getState().name();

		mockMvc.perform(get("/api/traffic/currentStateAndTimeHistoryOfEachDirection")).andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value(false)) // paused default
				.andExpect(jsonPath("$.currentStateForEachDirection.North-South").exists())
				.andExpect(jsonPath("$.currentStateForEachDirection.East-West").exists())
				.andExpect(jsonPath("$.timeHistoryOfEachDirection.North-South").isArray())
				.andExpect(jsonPath("$.timeHistoryOfEachDirection.East-West").isArray());
	}

	@Test
	void testHistoryAfterMultipleStateChanges() throws Exception {
		trafficController.getNorthSouth().getHistory().clear();
		trafficController.getEastWest().getHistory().clear();

		// Change sequence #1
		mockMvc.perform(post("/api/traffic/changeSequence").param("sequence", NS_GREEN_EW_RED))
				.andExpect(status().isOk());

		// Change sequence #2
		mockMvc.perform(post("/api/traffic/changeSequence").param("sequence", NS_YELLOW_EW_RED))
				.andExpect(status().isOk());

		// Change sequence #3
		mockMvc.perform(post("/api/traffic/changeSequence").param("sequence", NS_RED_EW_GREEN))
				.andExpect(status().isOk());

		// Change sequence #4
		mockMvc.perform(post("/api/traffic/changeSequence").param("sequence", EW_YELLOW_NS_RED))
				.andExpect(status().isOk());

		// Now check the status endpoint
		mockMvc.perform(get("/api/traffic/currentStateAndTimeHistoryOfEachDirection")).andExpect(status().isOk())
				// Current states should match last sequence
				.andExpect(jsonPath("$.currentStateForEachDirection.North-South").value(LightState.RED.name()))
				.andExpect(jsonPath("$.currentStateForEachDirection.East-West").value(LightState.YELLOW.name()))
				// History arrays should contain multiple entries
				.andExpect(jsonPath("$.timeHistoryOfEachDirection.North-South.length()").value(4))
				.andExpect(jsonPath("$.timeHistoryOfEachDirection.East-West.length()").value(4));
	}
}