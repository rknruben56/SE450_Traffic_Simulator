package sim.util;

import java.util.Random;

/**
 * Utility class used to get random values
 * 
 * @author rodri_000
 *
 */
public class Util {
	private Util() {
	}

	static private Random _RANDOM = new Random();

	static public final double _EPSILON = 1e-9;

	static public boolean isEquals(double x, double y) {
		return Math.abs(x - y) <= _EPSILON;
	}

	static public boolean isLessOrEquals(double x, double y) {
		return (x - y) <= _EPSILON;
	}

	static public boolean isLess(double x, double y) {
		return (x - y) < -_EPSILON;
	}

	static public void setRandomSeed(long seed) {
		_RANDOM.setSeed(seed);
	}

	static public double nextRandom(double min, double max) {
		if (Util.isLess(max, min))
			throw new IllegalArgumentException(max + "is smaller than " + min);
		return min + ((_RANDOM.nextDouble()) * (max - min));
	}
}
