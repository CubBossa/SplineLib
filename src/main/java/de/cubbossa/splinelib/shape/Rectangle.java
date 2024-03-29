package de.cubbossa.splinelib.shape;

import de.cubbossa.splinelib.util.Spline;
import de.cubbossa.splinelib.util.BezierVector;
import de.cubbossa.splinelib.util.Pose;
import de.cubbossa.splinelib.util.Vector;

public class Rectangle implements Shape {

	private final Pose pose;
	private final double sizeX;
	private final double sizeY;

	public Rectangle(Pose pose, double sizeX, double sizeY) {
		this.pose = pose;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	@Override
	public Pose getPose() {
		return pose;
	}

	@Override
	public Spline getSpline() {
		Vector base = pose.getPos();
		Vector a = pose.getDir().clone().normalize().multiply(sizeX / 2);
		Vector b = pose.getDir().crossProduct(pose.getUp()).clone().normalize().multiply(sizeY / 2);

		Spline result = new Spline(pose);
		result.setClosed(true);
		result.add(new BezierVector(base.clone().add(a).add(b), null, null));
		result.add(new BezierVector(base.clone().add(a).subtract(b), null, null));
		result.add(new BezierVector(base.clone().subtract(a).subtract(b), null, null));
		result.add(new BezierVector(base.clone().subtract(a).add(b), null, null));
		return result;
	}
}
