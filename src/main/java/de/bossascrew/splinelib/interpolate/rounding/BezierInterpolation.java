package de.bossascrew.splinelib.interpolate.rounding;

import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.util.BezierUtils;
import de.bossascrew.splinelib.util.BezierVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BezierInterpolation implements RoundingInterpolator<BezierVector, List<Vector>> {

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
	public List<List<Vector>> interpolate(List<BezierVector> points, boolean closedPath) {

		List<List<Vector>> result = new ArrayList<>();

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
			result.add(innerResult);
		}
		return result;
	}
}
