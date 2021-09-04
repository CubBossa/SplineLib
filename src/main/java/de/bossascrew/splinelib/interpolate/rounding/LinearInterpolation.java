package de.bossascrew.splinelib.interpolate.rounding;

import com.google.common.base.Preconditions;
import de.bossascrew.splinelib.Curve;
import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import org.bukkit.util.Vector;

import java.util.*;

public class LinearInterpolation implements RoundingInterpolator<List<BezierVector>, Map<BezierVector, Curve>> {

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
	public Map<BezierVector, Curve> interpolate(List<BezierVector> points, boolean closedPath) {
		Preconditions.checkArgument(!points.isEmpty());

		Map<BezierVector, Curve> result = new LinkedHashMap<>();
		for (int i = 0; i < points.size() + (closedPath ? 0 : -1); i++) {
			BezierVector left = points.get(i);
			BezierVector right = points.get(closedPath && i == points.size() - 1 ? 0 : i + 1);
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
