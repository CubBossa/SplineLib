package de.bossascrew.splinelib.shape;

import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.interpolate.SpacingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Pose;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Star implements Shape {

	private final Pose pose;
	private final int spikes;
	private final double innerRoundness;
	private final double outerRoundness;
	private final double innerRadius;
	private final double outerRadius;

	/**
	 * stars are likewise flowers if used in combination with bezier interpolation.
	 *
	 * @param pose           the position, direction and up vector
	 * @param spikes         how many spikes the star/flower will have.
	 * @param innerRoundness how rounded the inner spike is
	 * @param outerRoundness how flower-like the star will look
	 * @param innerRadius    the inner radius at which the spikes will start
	 * @param outerRadius    the outer radius and maximum size for spikes
	 */
	public Star(Pose pose, int spikes, double innerRoundness, double outerRoundness, double innerRadius, double outerRadius) {
		this.pose = pose;
		this.spikes = Integer.max(2, spikes);
		this.innerRoundness = innerRoundness;
		this.outerRoundness = outerRoundness;
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
	}

	@Override
	public boolean isPathClosedByDefault() {
		return true;
	}

	@Override
	public Pose getPose() {
		return pose;
	}

	@Override
	public List<BezierVector> getBezierVectors() {

		List<BezierVector> result = new ArrayList<>();
		Vector base = pose.getPos();

		Vector a = pose.getDir().clone().normalize();
		Vector b = a.clone().crossProduct(pose.getUp()).normalize();

		Vector aInner = a.clone().multiply(innerRadius);
		Vector bInner = b.clone().multiply(innerRadius);

		Vector aOuter = a.clone().multiply(outerRadius);
		Vector bOuter = b.clone().multiply(outerRadius);

		for (int i = 0; i < spikes; i++) {
			double degreeInner = 6.2831 / spikes * (i - 0.5);
			double degree = 6.2831 / spikes * i;
			Vector innerDir = aInner.clone().multiply(Math.sin(degreeInner)).add(bInner.clone().multiply(Math.cos(degreeInner)));
			Vector inner = base.clone().add(innerDir);
			Vector outerDir = aOuter.clone().multiply(Math.sin(degree)).add(bOuter.clone().multiply(Math.cos(degree)));
			Vector outer = base.clone().add(outerDir);

			Vector innerRight = innerDir.crossProduct(pose.getUp()).normalize().multiply(innerRoundness);
			Vector outerRight = outerDir.crossProduct(pose.getUp()).normalize().multiply(outerRoundness);

			result.add(new BezierVector(inner, inner.clone().add(innerRight), inner.clone().subtract(innerRight)));
			result.add(new BezierVector(outer, outer.clone().add(outerRight), outer.clone().subtract(outerRight)));
		}
		return result;
	}
}
