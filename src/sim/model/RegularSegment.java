package sim.model;

import sim.model.LightController.Direction;
import sim.swing.AnimatorBuilder;

final class RegularSegment implements SegmentStrategy {

	private static LightController[] _lc;
	private static Direction _d;
	private static int _index;
	private static AnimatorBuilder _builder;

	RegularSegment(LightController[] lc, Direction d, int index,
			AnimatorBuilder builder) {
		_lc = lc;
		_d = d;
		_index = index;
		_builder = builder;
	}

	public Source makeSegment() {
		CarAcceptor next = new Road(new Sink());
		if (_d == Direction.EASTWEST) {
			_builder.addHorizontalRoad((Road) next, _index, _lc.length, false);
		} else {
			_builder.addVerticalRoad((Road) next, _lc.length, _index, false);
		}
		for (int i = _lc.length - 1; i >= 0; i--) {
			Light l = new Light(next);
			_lc[i].setLight(l, _d);
			next = new Road(l);
			if (_d == Direction.EASTWEST) {
				_builder.addHorizontalRoad((Road) next, _index, i, false);
				_builder.addLight(l, _index, i);
			} else {
				_builder.addVerticalRoad((Road) next, i, _index, false);
				_builder.addLight(l, i, _index);
			}
		}
		return new Source(next);
	}
}
