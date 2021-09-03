package de.bossascrew.splinelib.interpolate;

import de.bossascrew.splinelib.interpolate.spacing.EquidistantInterpolation;
import de.bossascrew.splinelib.interpolate.spacing.NaturalInterpolation;
import de.bossascrew.splinelib.interpolate.rounding.BezierInterpolation;
import de.bossascrew.splinelib.interpolate.rounding.LinearInterpolation;

public class Interpolation {

	//type

	public static LinearInterpolation linearInterpolation(double distance) {
		return new LinearInterpolation(distance);
	}

	public static BezierInterpolation bezierInterpolation(int sampling) {
		return new BezierInterpolation(sampling);
	}

	//attribute

	public static NaturalInterpolation naturalInterpolation(int segments) {
		return new NaturalInterpolation(segments);
	}

	public static EquidistantInterpolation equidistantInterpolation(double spacing) {
		return new EquidistantInterpolation(spacing);
	}
}
