package de.cubbossa.splinelib.util;

import lombok.Getter;
import lombok.Setter;

public class BezierVector extends Vector {

	@Getter
	@Setter
	public Vector leftControlPoint;

	@Getter
	@Setter
	public Vector rightControlPoint;

	public BezierVector(Vector toCopy, Vector leftControlPoint, Vector rightControlPoint) {
		this(toCopy.getX(), toCopy.getY(), toCopy.getZ(), leftControlPoint, rightControlPoint);
	}

	public BezierVector(double x, double y, double z, Vector leftControlPoint, Vector rightControlPoint) {
		super(x, y, z);
		this.leftControlPoint = leftControlPoint;
		this.rightControlPoint = rightControlPoint;
	}

	public Vector toVector() {
		return new Vector(x, y, z);
	}
}
