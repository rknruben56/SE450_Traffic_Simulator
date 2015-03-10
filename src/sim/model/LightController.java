package sim.model;

import sim.agent.Agent;
import sim.agent.TimeServer;
import sim.agent.TimeServerLinked;
import sim.model.Light.FlowIndicator;

/**
 * Controller that controls the traffic lights using random values
 * 
 * @author rodri_000
 *
 */
public class LightController implements Agent {

	public enum Direction {
		EASTWEST, NORTHSOUTH
	}

	Light _eastWestLight;
	Light _northSouthLight;
	private TimeServer _time;

	LightController() {
		_time = TimeServerLinked.getInstance();
		enqueue();
	}

	private void enqueue() {
		_time.enqueue(_time.currentTime(), this);
	}
	
	/**
	 * USED FOR TESTING PURPOSES ONLY
	 * 
	 * @return
	 */
	public Light getEastWestLight(){
		return _eastWestLight;
	}
	
	/**
	 * USED FOR TESTING PURPOSES ONLY
	 * 
	 * @return
	 */
	public Light getNorthSouthLight(){
		return _northSouthLight;
	}

	public void setLight(Light l, Direction d) {
		if (d == Direction.EASTWEST) {
			_eastWestLight = l;
			_eastWestLight.setState(FlowIndicator.STOP);
		} else {
			_northSouthLight = l;
			_northSouthLight.setState(FlowIndicator.GO);
		}
	}

	public void run() {
		controlTraffic();
	}

	private void controlTraffic() {
		if (_eastWestLight.getFlow() == FlowIndicator.GO) {
			_northSouthLight.setState(FlowIndicator.STOP);
			_time.enqueue(
					_time.currentTime() + SP.TRAFFIC_GTIME.getValue(), this);
			_eastWestLight.setState(FlowIndicator.CAUTION);
		} else if (_eastWestLight.getFlow() == FlowIndicator.CAUTION) {
			_time.enqueue(
					_time.currentTime() + SP.TRAFFIC_YTIME.getValue(), this);
			_eastWestLight.setState(FlowIndicator.STOP);
			_northSouthLight.setState(FlowIndicator.GO);
		} else if (_northSouthLight.getFlow() == FlowIndicator.GO) {
			_eastWestLight.setState(FlowIndicator.STOP);
			_time.enqueue(
					_time.currentTime() + SP.TRAFFIC_GTIME.getValue(), this);
			_northSouthLight.setState(FlowIndicator.CAUTION);
		} else if (_northSouthLight.getFlow() == FlowIndicator.CAUTION) {
			_time.enqueue(
					_time.currentTime() + SP.TRAFFIC_YTIME.getValue(), this);
			_northSouthLight.setState(FlowIndicator.STOP);
			_eastWestLight.setState(FlowIndicator.GO);
		}
	}
}
