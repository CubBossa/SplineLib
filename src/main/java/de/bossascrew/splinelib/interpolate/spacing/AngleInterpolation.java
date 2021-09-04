package de.bossascrew.splinelib.interpolate.spacing;

import com.google.common.base.Preconditions;
import de.bossascrew.splinelib.Curve;
import de.bossascrew.splinelib.interpolate.SpacingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import lombok.Getter;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;

@Getter
public class AngleInterpolation implements SpacingInterpolator<Map<BezierVector, Curve>, Curve> {

	private final double angle;

	/**
	 * Sets points by angle. The algorithm is simplified to only decide for each sampled point if its angle is bigger than the provided angle.
	 * It does not add new points between sampled points yet
	 *
	 * @param angle to filter given sample points in degree
	 */
	public AngleInterpolation(double angle) {
		this.angle = angle * 0.01745329;
	}

	@Override
	public Curve interpolate(Map<BezierVector, Curve> points, boolean closedPath) {

		Curve result = new Curve();
		Curve combined = new Curve();

		points.forEach((bezierVector, vectors) -> combined.addAll(vectors));
		Preconditions.checkArgument(!combined.isEmpty());
		if (combined.size() == 1) {
			return new Curve(combined);
		}
		combined.add(combined.get(0));
		Vector lastAddedDir = combined.get(1).clone().subtract(combined.get(0));

		for (int i = 1; i < combined.size() - 1; i++) {
			Vector c1 = combined.get(i);
			Vector c2 = combined.get(i + 1);
			Vector cDir = c2.clone().subtract(c1);
			if (i == combined.size() - 2) {
				result.add(c2);
				continue;
			}
			if (angle(lastAddedDir, cDir) > angle) {
				result.add(c1);
				lastAddedDir = cDir;
			}
		}
		return result;
	}

	public static double angle(Vector a, Vector b) {
		Vector va = a.clone().normalize();
		Vector vb = b.clone().normalize();
		return Math.acos(va.dot(vb));
	}
}
