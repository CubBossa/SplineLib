package de.bossascrew.splinelib.interpolate.rounding;

import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.util.BezierUtils;
import de.bossascrew.splinelib.util.BezierVector;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BezierInterpolation implements RoundingInterpolator<List<BezierVector>, Map<BezierVector, List<Vector>>> {

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
	public Map<BezierVector, List<Vector>> interpolate(List<BezierVector> points, boolean closedPath) {

		Map<BezierVector, List<Vector>> result = new LinkedHashMap<>();

		for (int i = 0; i < points.size() + (closedPath ? 0 : -1); i++) {
			List<Vector> innerResult;
			BezierVector pointA = points.get(i);
			BezierVector pointB = points.get(closedPath && i == points.size() - 1 ? 0 : i + 1);

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
