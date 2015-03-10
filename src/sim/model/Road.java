package sim.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sim.agent.TimeServer;
import sim.agent.TimeServerLinked;

public class Road implements CarAcceptor {

	private Set<Car> _cars;
	private TimeServer _time;
	private double _endPosition;
	private CarAcceptor _next;

	Road(CarAcceptor ca) {
		_next = ca;
		_cars = new HashSet<Car>();
		_endPosition = SP.ROAD_LENGTH.getValue();
		_time = TimeServerLinked.getInstance();
	}

	public List<Car> getCars() {
		return new ArrayList<Car>(_cars);
	}

	public double getLength() {
		return _endPosition;
	}

	/**
	 * USED FOR TESTING PURPOSES
	 * 
	 * @return
	 */
	public CarAcceptor getNext() {
		return _next;
	}

	/**
	 * USED FOR TESTING PURPOSES
	 * 
	 * @param Length l
	 */
	public void setLength(double l) {
		_endPosition = l;
	}

	private double distanceToCarBack(double fromPosition) {
		double carBackPosition = Double.POSITIVE_INFINITY;
		for (Car c : _cars) {
			if (c.getBackPosition() >= fromPosition
					&& c.getBackPosition() < carBackPosition)
				carBackPosition = c.getBackPosition();
		}
		return carBackPosition;
	}

	public double distanceToObstacle(double fromPosition) {
		double obstaclePosition = this.distanceToCarBack(fromPosition);
		if (obstaclePosition == Double.POSITIVE_INFINITY) {
			double distanceToEnd = Math.abs(_endPosition - fromPosition);
			if (_next != null) {
				obstaclePosition = (_next).distanceToObstacle(Math
						.abs(_endPosition - -fromPosition));
				if (obstaclePosition == 0.0) {
					return distanceToEnd;
				}
			} else {
				return distanceToEnd;
			}
		}

		return obstaclePosition - fromPosition;
	}

	public boolean accept(Car c, double frontPosition) {
		_cars.remove(c);
		if (frontPosition > _endPosition) {
			if (_next != null) {
				return _next.accept(c, frontPosition - _endPosition);
			} else {
				return false;
			}
		} else {
			c.setCurrentRoad(this);
			c.setFrontPosition(frontPosition);
			_cars.add(c);
			_time.enqueue(_time.currentTime() + SP.TIME_STEP.getValue(), c);
			return true;
		}
	}
}
