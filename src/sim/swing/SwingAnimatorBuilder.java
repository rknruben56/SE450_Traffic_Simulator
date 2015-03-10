package sim.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sim.model.SP;
import sim.swing.AnimatorBuilder;
import sim.model.Car;
import sim.model.Light;
import sim.model.Light.FlowIndicator;
import sim.model.Road;
import sim.util.Animator;
import sim.util.SwingAnimator;
import sim.util.SwingAnimatorPainter;

/**
 * A class for building Animators.
 */
public class SwingAnimatorBuilder implements AnimatorBuilder {
	MyPainter _painter;

	public SwingAnimatorBuilder() {
		_painter = new MyPainter();
	}

	public Animator getAnimator() {
		if (_painter == null) {
			throw new IllegalStateException();
		}
		Animator returnValue = new SwingAnimator(_painter, "Traffic Simulator",
				VP.displayWidth, VP.displayHeight, VP.displayDelay);
		_painter = null;
		return returnValue;
	}

	private static double skipInit = VP.gap;
	private static double skipRoad = VP.gap + SP.ROAD_LENGTH_UI.getValue();
	// SP.ROAD_LENGTH_UI.getValue();
	private static double skipCar = VP.gap + VP.elementWidth;
	private static double skipRoadCar = skipRoad + skipCar;

	public void addLight(Light d, int i, int j) {
		double x = skipInit + skipRoad + j * skipRoadCar;
		double y = skipInit + skipRoad + i * skipRoadCar;
		Translator t = new TranslatorWE(x, y, SP.CAR_WIDTH_UI.getValue(),
				VP.elementWidth, VP.scaleFactor);
		_painter.addLight(d, t);
	}

	public void addHorizontalRoad(Road l, int i, int j, boolean eastToWest) {
		double x = skipInit + j * skipRoadCar;
		double y = skipInit + skipRoad + i * skipRoadCar;
		Translator t = eastToWest ? new TranslatorEW(x, y,
				SP.ROAD_LENGTH_UI.getValue(), VP.elementWidth, VP.scaleFactor)
				: new TranslatorWE(x, y, SP.ROAD_LENGTH_UI.getValue(),
						VP.elementWidth, VP.scaleFactor);
		_painter.addRoad(l, t);
	}

	public void addVerticalRoad(Road l, int i, int j, boolean southToNorth) {
		double x = skipInit + skipRoad + j * skipRoadCar;
		double y = skipInit + i * skipRoadCar;
		Translator t = southToNorth ? new TranslatorSN(x, y,
				SP.ROAD_LENGTH_UI.getValue(), VP.elementWidth, VP.scaleFactor)
				: new TranslatorNS(x, y, SP.ROAD_LENGTH_UI.getValue(),
						VP.elementWidth, VP.scaleFactor);
		_painter.addRoad(l, t);
	}

	/** Class for drawing the Model. */
	private static class MyPainter implements SwingAnimatorPainter {

		/**
		 * Pair of a model element <code>x</code> and a translator
		 * <code>t</code>.
		 */
		private static class Element<T> {
			T x;
			Translator t;

			Element(T x, Translator t) {
				this.x = x;
				this.t = t;
			}
		}

		private List<Element<Road>> _roadElements;
		private List<Element<Light>> _lightElements;

		MyPainter() {
			_roadElements = new ArrayList<Element<Road>>();
			_lightElements = new ArrayList<Element<Light>>();
		}

		void addLight(Light x, Translator t) {
			_lightElements.add(new Element<Light>(x, t));
		}

		void addRoad(Road x, Translator t) {
			_roadElements.add(new Element<Road>(x, t));
		}

		public void paint(Graphics g) {
			// This method is called by the swing thread, so may be called
			// at any time during execution...

			// First draw the background elements
			for (Element<Light> e : _lightElements) {
				if (e.x.getFlow() == FlowIndicator.GO) {
					g.setColor(Color.GREEN);
				} else if (e.x.getFlow() == FlowIndicator.CAUTION) {
					g.setColor(Color.YELLOW);
				} else if (e.x.getFlow() == FlowIndicator.STOP) {
					g.setColor(Color.RED);
				}
				XGraphics.fillOval(g, e.t, 0, 0, SP.CAR_WIDTH_UI.getValue(),
						VP.elementWidth);
			}
			g.setColor(Color.BLACK);
			for (Element<Road> e : _roadElements) {
				XGraphics.fillRect(g, e.t, 0, 0, SP.ROAD_LENGTH_UI.getValue(),
						VP.elementWidth);
			}

			// Then draw the foreground elements
			for (Element<Road> e : _roadElements) {
				// iterate through a copy because e.x.getCars() may change
				// during iteration...
				double roadLength = e.x.getLength();
				Iterator<Car> it = e.x.getCars().iterator();
				while (it.hasNext()) {
					Car d = it.next();
					g.setColor(d.getColor());
					double normalizedPosition = d.getFrontPosition()
							/ roadLength;
					XGraphics.fillOval(g, e.t, normalizedPosition
							* SP.ROAD_LENGTH_UI.getValue(), 0, d.getLength(),
							VP.elementWidth);
				}
			}
		}
	}
}
