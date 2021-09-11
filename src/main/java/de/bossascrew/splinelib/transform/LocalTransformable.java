package de.bossascrew.splinelib.transform;

import de.bossascrew.splinelib.util.Vector;

public interface LocalTransformable<T> {

	T rotateLocalX(double degree);

	T rotateLocalXRadian(double radian);

	T rotateLocalY(double degree);

	T rotateLocalYRadian(double radian);

	T rotateLocalZ(double degree);

	T rotateLocalZRadian(double radian);

	T scaleLocal(double x, double y, double z);

	T scaleLocal(Vector pivot, double x, double y, double z);

	T translateLocal(Vector vector);

	T translateLocal(double x, double y, double z);
}
