package provided.utils.valueGenerator.impl;

import provided.utils.valueGenerator.ISineMaker;

/**
 * A utility class that can be used to create smoothly varying sinusoidal
 * numerical data.
 * Note that this is not a singleton class nor does it have static methods as
 * its behavior depends on the minimum, maximum and delta values given to 
 * its constructor.
 */
public class SineMaker implements ISineMaker {
	/**
	 * The midpoint of the min and max values.
	 */
	private double _mid;
	/**
	 * The amplitude of the sinusoidal output
	 */
	private double _ampl;
	/**
	 * The amount that _theta is incremented each time, in radians.
	 */
	private double _delta;
	/**
	 * The current angle used to generate the current
	 * sinusoidal value.  Initialized to produce the minimum value.
	 */
	private double _theta = -Math.PI / 2.0;

	/**
	 * The constructor takes several values to control the object's behavior.
	 * @param min The minimum and initial value to produce.
	 * @param max The maximum value to produce.
	 * @param delta The amount in radians that the generating angle is incremented each time
	 * */
	public SineMaker(double min, double max, double delta) {
		_mid = (max + min) / 2.0;
		_ampl = (max - min) / 2.0;
		_delta = delta;
	}

	/* (non-Javadoc)
	 * @see provided.utils.valueGenerator.ISineMaker#getDblVal()
	 */
	@Override
	public double getDblVal() {
		double result = _mid + _ampl * Math.sin(_theta);
		_theta += _delta; // shorthand for _theta = _theta + _delta
		return result;
	}

	/* (non-Javadoc)
	 * @see provided.utils.valueGenerator.ISineMaker#getIntVal()
	 */
	@Override
	public int getIntVal() {
		return (int) Math.round(getDblVal());
	}
}