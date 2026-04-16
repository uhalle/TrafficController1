package com.example.demo;

public class TrafficLight {
	
	public enum LightState {
	    RED, GREEN, YELLOW;
	    
	    public LightState next() {
	        switch(this) {
	            case RED: return GREEN;
	            case GREEN: return YELLOW;
	            case YELLOW: return RED;
	            default: throw new IllegalStateException();
	        }
	    }
	}
	
	 private LightState state = LightState.RED;
	 private String direction;

	    public TrafficLight(DIRECTION direction) {
	        this.direction = direction.direction;
	    }
	    
	    public void changeState() {
	        state = state.next();
	        System.out.println("Light changed to: " + state);
	    }
	    
	    public LightState getState() {
	        return state;
	    }
	    
	    public void setState(LightState lightState) {
	        this.state = lightState;
	    }

		public String getDirection() {
			return direction;
		}

}
