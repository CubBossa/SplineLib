package de.bossascrew.splinelib.util;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class VectorUtil {

	public void mirror(Vector point, Vector v, Vector w, Vector... vectorsToMirror) {
		mirror(point, v, w, List.of(vectorsToMirror));
	}

	public void mirror(Vector point, Vector v, Vector w, Collection<? extends Vector> vectorsToMirror) {
		Vector n = v.clone().crossProduct(w);
		double d = n.x * point.x + n.y * point.y + n.z * point.z;
		for(Vector vec : vectorsToMirror) {
			vec.subtract(n.clone().multiply(2).multiply(n.clone().dot(vec) - d));
		}
	}

	public void rotate(Vector point, Vector axis, double angleInRadian, Vector... vectorsToRotate) {

		axis = axis.isNormalized() ? axis : axis.normalize();

		double cosTheta = Math.cos(angleInRadian);
		double sinTheta = Math.sin(angleInRadian);
		double x2 = axis.getX();
		double y2 = axis.getY();
		double z2 = axis.getZ();

		for (Vector toRotate : vectorsToRotate) {
			toRotate.subtract(point);
			double x = toRotate.getX();
			double y = toRotate.getY();
			double z = toRotate.getZ();
			double dotProduct = toRotate.dot(axis);
			double xPrime = x2 * dotProduct * (1.0D - cosTheta) + x * cosTheta + (-z2 * y + y2 * z) * sinTheta;
			double yPrime = y2 * dotProduct * (1.0D - cosTheta) + y * cosTheta + (z2 * x - x2 * z) * sinTheta;
			double zPrime = z2 * dotProduct * (1.0D - cosTheta) + z * cosTheta + (-y2 * x + x2 * y) * sinTheta;
			toRotate.setX(xPrime).setY(yPrime).setZ(zPrime);
			toRotate.add(point);
		}
	}
}
