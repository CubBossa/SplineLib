package de.cubbossa.splinelib.interpolate.rounding;

import de.cubbossa.splinelib.util.Curve;
import de.cubbossa.splinelib.util.Spline;
import de.cubbossa.splinelib.interpolate.RoundingInterpolator;
import de.cubbossa.splinelib.util.BezierUtils;
import de.cubbossa.splinelib.util.BezierVector;

import java.util.LinkedHashMap;
import java.util.Map;

public class BezierInterpolation implements RoundingInterpolator<Spline, Map<BezierVector, Curve>> {

	private final int sampling;

	/**
	 * a path interpolator that will read the BezierVector controll points to calculate a bezier path
	 * curved parts of the spline will have more points with less spacing. To evenly spread them on the spline use EquidistantInterpolation
	 *
	 * @param sampling how many steps to calculate for each segment.
	 */
	public BezierInterpolation(int sampling) {
		this.sampling = sampling;
	}

	@Override
	public Map<BezierVector, Curve> interpolate(Spline points, boolean closedPath) {

		Map<BezierVector, Curve> result = new LinkedHashMap<>();

		for (int i = 0; i < points.size() + (closedPath ? 0 : -1); i++) {
			Curve innerResult;
			BezierVector pointA = points.get(i);
			BezierVector pointB = points.get(i + 1);

			if (pointA.getRightControlPoint() == null) {
				if (pointB.getLeftControlPoint() == null) {
					innerResult = BezierUtils.getBezierCurve(sampling, pointA, pointB);
				} else {
					innerResult = BezierUtils.getBezierCurve(sampling, pointA, pointB, pointB.getLeftControlPoint());
				}
			} else {
				if (pointB.getLeftControlPoint() == null) {
					innerResult = BezierUtils.getBezierCurve(sampling, pointA, pointB, pointA.getRightControlPoint());
				} else {
					innerResult = BezierUtils.getBezierCurve(sampling, pointA, pointB, pointA.getRightControlPoint(), pointB.getLeftControlPoint());
				}
			}
			//adding last point to achieve closed path.
			if (i == points.size() + (closedPath ? -1 : -2)) {
				innerResult.add(pointB.toVector());
			}
			result.put(pointA, innerResult);
		}
		return result;
	}
}
