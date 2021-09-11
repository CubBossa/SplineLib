package de.bossascrew.splinelib.interpolate.rounding;

import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Curve;
import de.bossascrew.splinelib.util.Spline;
import de.bossascrew.splinelib.util.Vector;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LinearInterpolation implements RoundingInterpolator<Spline, Map<BezierVector, Curve>> {

	private final double distance;

	/**
	 * a path interpolator that will connect input points with straight lines, regardless of any bezier control points
	 *
	 * @param distance defines the space between two points
	 */
	public LinearInterpolation(double distance) {
		this.distance = distance;
	}

	@Override
	public Map<BezierVector, Curve> interpolate(Spline points, boolean closedPath) {
		Map<BezierVector, Curve> result = new LinkedHashMap<>();
		if (points.isEmpty()) {
			return result;
		}

		for (int i = 0; i < points.size() + (closedPath ? 0 : -1); i++) {
			BezierVector left = points.get(i);
			BezierVector right = points.get(i + 1);
			Vector dir = right.clone().subtract(left);

			Curve innerResult = new Curve();

			double dirLength = dir.length();
			for (double dst = 0; dst < dirLength; dst += distance) {
				Vector lerped = left.clone().add(dir.clone().multiply(dst / dirLength));
				innerResult.add(lerped);
			}
			result.put(left, innerResult);
		}
		return result;
	}
}
