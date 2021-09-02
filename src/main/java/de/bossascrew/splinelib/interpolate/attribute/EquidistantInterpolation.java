package de.bossascrew.splinelib.interpolate.attribute;

import com.google.common.base.Preconditions;
import de.bossascrew.splinelib.interpolate.Interpolator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class EquidistantInterpolation implements Interpolator<List<Vector>, Vector> {

	private final double spacing;

	public EquidistantInterpolation(double spacing) {
		this.spacing = spacing;
	}

	@Override
	public List<Vector> interpolate(List<List<Vector>> points) {
		Preconditions.checkArgument(!points.isEmpty());

		List<Vector> combined = new ArrayList<>();
		points.forEach(combined::addAll);

		List<Vector> result = new ArrayList<>();
		result.add(combined.get(0));
		Vector prevPoint = combined.get(0);
		double distanceLastEvenPoint = 0;

		for (int i = 1; i < combined.size() - 1; i++) {
			Vector pointOnCurve = combined.get(i);
			distanceLastEvenPoint += prevPoint.distance(pointOnCurve);

			while (distanceLastEvenPoint >= spacing) {
				double overShootDist = distanceLastEvenPoint - spacing;
				Vector newEvenlySpacedPoint = pointOnCurve.clone().add(prevPoint.clone().subtract(pointOnCurve).normalize().multiply(overShootDist));
				result.add(newEvenlySpacedPoint);
				distanceLastEvenPoint = overShootDist;
				prevPoint = newEvenlySpacedPoint;
			}
			prevPoint = pointOnCurve;
		}
		return result;
	}
}
