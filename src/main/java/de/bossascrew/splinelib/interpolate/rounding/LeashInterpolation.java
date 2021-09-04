package de.bossascrew.splinelib.interpolate.rounding;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LeashInterpolation implements RoundingInterpolator<List<BezierVector>, Map<BezierVector, List<Vector>>> {

	private final int samples;

	public LeashInterpolation(int samples) {
		this.samples = samples;
	}


	@Override
	public Map<BezierVector, List<Vector>> interpolate(List<BezierVector> points, boolean closedPath) {

		Map<BezierVector, List<Vector>> result = Maps.newLinkedHashMap();
		if (points.size() == 1) {
			result.put(points.get(0), Lists.newArrayList(points.get(0).toVector()));
			return result;
		}

		for (int i = 0; i < points.size() + (closedPath ? 0 : -1); i++) {
			BezierVector a = points.get(i);
			BezierVector b = points.get(i == points.size() - 1 ? 0 : i + 1);

			result.put(a, getParabolaPoints(a, b));
		}
		return result;
	}

	/**
	 * returns parabola for segment between two bezier points
	 */
	public List<Vector> getParabolaPoints(Vector start, Vector end) {
		List<Vector> result = new ArrayList<>();
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
