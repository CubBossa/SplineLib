package de.cubbossa.splinelib.interpolate;

import de.cubbossa.splinelib.util.Curve;
import de.cubbossa.splinelib.interpolate.rounding.BezierInterpolation;
import de.cubbossa.splinelib.interpolate.rounding.LeashInterpolation;
import de.cubbossa.splinelib.interpolate.rounding.LinearInterpolation;
import de.cubbossa.splinelib.interpolate.spacing.AngleInterpolation;
import de.cubbossa.splinelib.interpolate.spacing.EquidistantInterpolation;
import de.cubbossa.splinelib.interpolate.spacing.NaturalInterpolation;
import de.cubbossa.splinelib.util.BezierVector;

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
