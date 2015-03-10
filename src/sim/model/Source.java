package sim.model;

import sim.agent.Agent;
import sim.agent.TimeServer;
import sim.agent.TimeServerLinked;

/**
 * Starting point of the road that creates Cars between the specified random
 * intervals
 * 
 * @author rodri_000
 *
 */
public final class Source implements Agent {

	private CarAcceptor _next;
	private TimeServer _time;

	Source(CarAcceptor ca) {
		_next = ca;
		_time = TimeServerLinked.getInstance();
		enqueue();
	}

	private void enqueue() {
		_time.enqueue(_time.currentTime() + SP.CAR_GEN_DELAY.getValue(), this);

	}

	public void run() {
		Car c = CarFactory.createCar();
		if (_next.accept(c, c.getFrontPosition())
				&& _next.distanceToObstacle(c.getFrontPosition()) > c
						.getFrontPosition()) {
			_time.enqueue(_time.currentTime() + SP.CAR_GEN_DELAY.getValue(),
					this);
		}
	}
}
