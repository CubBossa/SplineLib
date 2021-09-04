package de.bossascrew.splinelib.interpolate;

import de.bossascrew.splinelib.Curve;
import de.bossascrew.splinelib.interpolate.rounding.BezierInterpolation;
import de.bossascrew.splinelib.interpolate.rounding.LeashInterpolation;
import de.bossascrew.splinelib.interpolate.rounding.LinearInterpolation;
import de.bossascrew.splinelib.interpolate.spacing.AngleInterpolation;
import de.bossascrew.splinelib.interpolate.spacing.EquidistantInterpolation;
import de.bossascrew.splinelib.interpolate.spacing.NaturalInterpolation;
import de.bossascrew.splinelib.util.BezierVector;

import java.util.Map;

public class Interpolation {

	//round

	public static LinearInterpolation linearInterpolation(double distance) {
		return new LinearInterpolation(distance);
	}

	public static BezierInterpolation bezierInterpolation(int sampling) {
		return new BezierInterpolation(sampling);
	}

	public static LeashInterpolation leashInterpolation(int sampling) {
		return new LeashInterpolation(sampling);
	}

	//spacing

	public static SpacingInterpolator<Map<BezierVector, Curve>, Curve> naturalInterpolation(int segments) {
		return new NaturalInterpolation(segments);
	}

	public static EquidistantInterpolation equidistantInterpolation(double spacing) {
		return new EquidistantInterpolation(spacing);
	}

	public static AngleInterpolation angularInterpolation(double angle) {
		return new AngleInterpolation(angle);
	}
}
