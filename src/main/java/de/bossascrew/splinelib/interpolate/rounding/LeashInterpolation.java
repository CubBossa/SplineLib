package de.bossascrew.splinelib.interpolate.rounding;

import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Curve;
import de.bossascrew.splinelib.util.Spline;
import de.bossascrew.splinelib.util.Vector;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class LeashInterpolation implements RoundingInterpolator<Spline, Map<BezierVector, Curve>> {

	private final int samples;

	public LeashInterpolation(int samples) {
		this.samples = samples;
	}


	@Override
	public Map<BezierVector, Curve> interpolate(Spline points, boolean closedPath) {

		Map<BezierVector, Curve> result = new LinkedHashMap<>();
		if (points.size() == 1) {
			result.put(points.get(0), new Curve(points.get(0).toVector()));
			return result;
		}

		for (int i = 0; i < points.size() + (closedPath ? 0 : -1); i++) {
			BezierVector a = points.get(i);
			BezierVector b = points.get(i + 1);

			result.put(a, getParabolaPoints(a, b));
		}
		return result;
	}

	/**
	 * returns parabola for segment between two bezier points
	 */
	public Curve getParabolaPoints(Vector start, Vector end) {
		Curve result = new Curve();
		boolean isStartLower = start.getY() < end.getY();
		Vector s = isStartLower ? start : end;
		Vector v = isStartLower ? end : start;

		Vector dir = v.clone().subtract(s);
		Vector dirFlat = dir.clone().setY(s.getY());

		// a = factor, sx = s.getXZ(), sy = s.getY()
		// f(x) = a * (x - sx)² + sy
		// a = (y - sy) / (x - sx)²
		double factor = (v.getY() - s.getY()) / Math.pow(dirFlat.length(), 2);
		for (int i = 0; i < samples; i++) {
			double progress = (double) i / samples;
			result.add(s.clone()
					// using the actual formula after calculating factor
					.add(dirFlat.clone().multiply(new Vector(progress, 1, progress)))
					.setY(factor * Math.pow(progress * dirFlat.length(), 2) + s.getY())
			);
		}
		if (!isStartLower) {
			Collections.reverse(result);
		}
		return result;
	}
}
