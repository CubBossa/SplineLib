package de.bossascrew.splinelib.shape;

import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Pose;
import de.bossascrew.splinelib.util.Spline;
import de.bossascrew.splinelib.util.Vector;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Heart implements Shape {

	private static final double C = 0.551915024494;

	private Pose pose;
	private double length;
	private double height;
	private double roundness;

	public Heart(Pose pose, double length, double height, double roundness) {
		this.pose = pose;
		this.length = length;
		this.height = height;
		this.roundness = roundness;
	}

	@Override
	public Spline getSpline() {

		Vector b = pose.getPos();
		Vector d = pose.getUp().clone().normalize();
		Vector r = d.clone().crossProduct(pose.getDir()).normalize();

		Spline spline = new Spline(pose);
		spline.setClosed(true);

		Vector vRight = r.clone().multiply(length / 2);
		Vector rl = b.clone().add(d.clone().multiply(1d / 3 * height * C));
		Vector nrl = b.clone().subtract(d.clone().multiply(1d / 3 * height * C));

		Vector lowerBase = b.clone().subtract(d.clone().multiply(height / 3 * 2));
		Vector upperLeft = b.clone().subtract(vRight.clone().multiply(0.5)).add(d.clone().multiply(1d / 3 * height));
		Vector upperRight = b.clone().add(vRight.clone().multiply(0.5)).add(d.clone().multiply(1d / 3 * height));
		Vector upperBezier = r.clone().multiply(1d / 3 * length * C);

		spline.add(new BezierVector(b.clone().subtract(vRight), nrl.clone().subtract(vRight), rl.clone().subtract(vRight)));
		spline.add(new BezierVector(upperLeft, upperLeft.clone().subtract(upperBezier), upperLeft.clone().add(upperBezier)));
		spline.add(new BezierVector(b.clone(), rl, rl.clone()));
		spline.add(new BezierVector(upperRight, upperRight.clone().subtract(upperBezier), upperRight.clone().add(upperBezier)));
		spline.add(new BezierVector(b.clone().add(vRight), rl.clone().add(vRight), nrl.clone().add(vRight)));
		spline.add(new BezierVector(lowerBase, lerp(b, lowerBase.clone().add(vRight), roundness), lerp(b, lowerBase.clone().subtract(vRight), roundness)));
		return spline;
	}

	private Vector lerp(Vector a, Vector b, double t) {
		t = Double.min(1, Double.max(0, t));
		return Vector.zero().clone().add(a.clone().multiply(1 - t)).add(b.clone().multiply(t));
	}
}
