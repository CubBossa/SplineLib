package de.bossascrew.splinelib.interpolate.spacing;

import de.bossascrew.splinelib.interpolate.SpacingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Curve;
import de.bossascrew.splinelib.util.Vector;

import java.util.Map;

public class EquidistantInterpolation implements SpacingInterpolator<Map<BezierVector, Curve>, Curve> {

	private final double spacing;

	public EquidistantInterpolation(double spacing) {
		this.spacing = spacing;
	}

	@Override
	public Curve interpolate(Map<BezierVector, Curve> points, boolean closedPath) {

		Curve combined = new Curve();
		points.forEach((bezierVector, vectors) -> combined.addAll(vectors));
		if (combined.isEmpty()) {
			return combined;
		}

		Curve result = new Curve();
		result.add(combined.get(0));
		Vector prevPoint = combined.get(0);
		double distanceLastEvenPoint = 0;

		for (int i = 1; i < combined.size() + (closedPath ? 1 : 0); i++) {
			Vector pointOnCurve = combined.get(i == combined.size() ? 0 : i);
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
