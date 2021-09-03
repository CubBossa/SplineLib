package de.bossascrew.splinelib.interpolate.rounding;

import com.google.common.base.Preconditions;
import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LinearInterpolation implements RoundingInterpolator<BezierVector, List<Vector>> {

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
	public List<List<Vector>> interpolate(List<BezierVector> points, boolean closedPath) {
		Preconditions.checkArgument(!points.isEmpty());

		List<List<Vector>> result = new ArrayList<>();
		for (int i = 0; i < points.size() + (closedPath ? 0 : -1); i++) {
			BezierVector left = points.get(i);
			BezierVector right = points.get(closedPath && i == points.size() - 1 ? 0 : i + 1);
			Vector dir = right.clone().subtract(left);

			List<Vector> innerResult = new ArrayList<>();

			double dirLength = dir.length();
			for (double dst = 0; dst < dirLength; dst += distance) {
				Vector lerped = left.clone().add(dir.multiply(dst / dirLength));
				innerResult.add(lerped);
			}
			result.add(innerResult);
		}
		return result;
	}
}
