package de.bossascrew.splinelib.shape;

import de.bossascrew.splinelib.Spline;
import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Pose;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.util.Vector;

@RequiredArgsConstructor
@Getter
public class Oval implements Shape {

	private static final double C = 0.551915024494;

	private final Pose pose;
	private final double radius;
	private final double ratio;

	@Override
	public Pose getPose() {
		return pose;
	}

	@Override
	public Spline getSpline() {

		Vector b = pose.getPos();
		Vector d = pose.getDir().clone().normalize();
		Vector r = d.clone().crossProduct(pose.getUp()).normalize();

		Spline result = new Spline();
		result.setClosed(true);

		Vector vUpper = b.clone().add(d.clone().multiply(radius));
		Vector vLower = b.clone().subtract(d.clone().multiply(radius));
		Vector vB = r.clone().multiply(C * radius * ratio);

		Vector vRight = b.clone().add(r.clone().multiply(radius * ratio));
		Vector vLeft = b.clone().subtract(r.clone().multiply(radius * ratio));
		Vector v2B = d.clone().multiply(C * radius);

		result.add(new BezierVector(vLower, vLower.clone().subtract(vB), vLower.clone().add(vB)));
		result.add(new BezierVector(vRight, vRight.clone().subtract(v2B), vRight.clone().add(v2B)));
		result.add(new BezierVector(vUpper, vUpper.clone().add(vB), vUpper.clone().subtract(vB)));
		result.add(new BezierVector(vLeft, vLeft.clone().add(v2B), vLeft.clone().subtract(v2B)));

		return result;
	}
}
