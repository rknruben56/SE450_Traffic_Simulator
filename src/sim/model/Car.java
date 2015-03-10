package sim.model;

import java.awt.Color;

import sim.agent.Agent;

public class Car implements Agent {

	private static final double _INITIAL_POS = 0.0;
	private double _frontPosition;
	private double _maxVelocity;
	private double _brakeDistance;
	private double _stopDistance;
	private double _length;
	private Color _color;
	private Road _currentRoad;

	Car() {
		_frontPosition = _INITIAL_POS;
		_maxVelocity = SP.CAR_MAX_VEL.getValue();
		_brakeDistance = SP.CAR_BRAKE_DIST.getValue();
		_stopDistance = SP.CAR_STOP_DIST.getValue();
		_length = SP.CAR_LENGTH.getValue();
		_color = getRandColor();
	}

	private Color getRandColor() {
		return new java.awt.Color((int) Math.ceil(Math.random() * 255),
				(int) Math.ceil(Math.random() * 255), (int) Math.ceil(Math
						.random() * 255));
	}

	public Road getCurrentRoad() {
		return _currentRoad;
	}

	public void setCurrentRoad(Road road) {
		_currentRoad = road;
	}

	public double getFrontPosition() {
		return _frontPosition;
	}

	public void setFrontPosition(double frontPosition) {
		_frontPosition = frontPosition;
	}

	public double getBackPosition() {
		return _frontPosition - _length;
	}

	public Color getColor() {
		return _color;
	}

	public double getLength() {
		return _length;
	}

	private double newVelocity(double closestObstacle) {
		return (_maxVelocity / (_brakeDistance - _stopDistance))
				* (closestObstacle - _stopDistance);
	}

	public void run() {
		double velocity = 0;
		double closestObstacle = getCurrentRoad().distanceToObstacle(
				getFrontPosition());

		if ((closestObstacle < _maxVelocity)
				&& (closestObstacle > _brakeDistance)) {
			velocity = newVelocity(closestObstacle);
		} else if (closestObstacle <= _stopDistance) {
			velocity = 0;
		} else if (closestObstacle > _maxVelocity) {
			velocity = _maxVelocity;
		}

		_frontPosition += velocity * SP.TIME_STEP.getValue();
		getCurrentRoad().accept(this, _frontPosition);
	}

}
