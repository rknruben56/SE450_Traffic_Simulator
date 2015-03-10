package sim.model;

/**
 * Light that sets the flow for the traffic
 * 
 * @author rodri_000
 *
 */
public class Light implements CarAcceptor {
	// Enum used to control traffic
	public enum FlowIndicator {
		GO, CAUTION, STOP
	}

	private FlowIndicator _state;
	private CarAcceptor _nextRoad;

	Light(CarAcceptor ca) {
		_nextRoad = ca;
	}

	public FlowIndicator getFlow() {
		return _state;
	}

	public void setState(FlowIndicator f) {
		_state = f;
	}

	public double distanceToObstacle(double fromPosition) {
		if (_state == FlowIndicator.GO || _state == FlowIndicator.CAUTION) {
			return _nextRoad.distanceToObstacle(fromPosition);
		} else {
			return 0.0;
		}
	}

	public boolean accept(Car c, double frontPosition) {
		if (_state == FlowIndicator.GO || _state == FlowIndicator.CAUTION) {
			return _nextRoad.accept(c, frontPosition);
		} else {
			return false;
		}
	}
}
