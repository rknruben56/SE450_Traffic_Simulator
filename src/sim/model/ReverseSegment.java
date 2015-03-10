package sim.model;

import sim.model.LightController.Direction;
import sim.swing.AnimatorBuilder;

public final class ReverseSegment implements SegmentStrategy {

	private static LightController[] _lc;
	private static Direction _d;
	private static int _index;
	private static AnimatorBuilder _builder;

	ReverseSegment(LightController[] lc, Direction d, int index,
			AnimatorBuilder builder) {
		_lc = lc;
		_d = d;
		_index = index;
		_builder = builder;
	}

	public Source makeSegment() {
		CarAcceptor next = new Road(new Sink());
		for (int i = 0; i < _lc.length; i++) {
			Light l = new Light(next);
			_lc[i].setLight(l, _d);
			if (_d == Direction.EASTWEST) {
				_builder.addLight(l, _index, i);
				_builder.addHorizontalRoad((Road) next, _index, i, true);
			} else {
				_builder.addLight(l, i, _index);
				_builder.addVerticalRoad((Road) next,i, _index, true);
			}
			next = new Road(l);
		}
		if (_d == Direction.EASTWEST) {
			_builder.addHorizontalRoad((Road) next, _index, _lc.length, true);
		} else {
			_builder.addVerticalRoad((Road) next, _lc.length, _index, true);
		}
		return new Source(next);
	}
}
