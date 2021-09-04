package de.bossascrew.splinelib.util;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Transformable<E extends Vector, V extends Vector> extends ArrayList<E> {

	public Transformable() {

	}

	public Transformable(E element) {
		super();
		this.add(element);
	}

	public Transformable(Collection<E> collection) {
		super(collection);
	}

	public Transformable<E, V> mirror(Vector point) {
		for (V vector : getVectors()) {
			vector.add(point.clone().subtract(vector).multiply(2));
		}
		return this;
	}

	public Transformable<E, V> mirror(Vector point, Vector axis) {
		rotate(point, axis, 180);
		return this;
	}

	public Transformable<E, V> mirror(Vector point, Vector v, Vector w) {
		// check if vectors are similar (v = x * w)
		Vector toTest = v.clone().normalize();

		if (!(w.clone().normalize().equals(toTest) || w.clone().normalize().equals(toTest.multiply(-1)))) {
			throw new IllegalArgumentException("Vectors are similar (v = x * w) and cannot represent a plane.");
		}

		//TODO
		return this;
	}

	public Transformable<E, V> rotate(Vector point, @NonNull Vector axis, double angle) {
		angle *= 0.01745329;

		axis = axis.isNormalized() ? axis : axis.normalize();

		double cosTheta = Math.cos(angle);
		double sinTheta = Math.sin(angle);
		double x2 = axis.getX();
		double y2 = axis.getY();
		double z2 = axis.getZ();

		for (V vector : getVectors()) {
			vector.subtract(point);
			double x = vector.getX();
			double y = vector.getY();
			double z = vector.getZ();
			double dotProduct = vector.dot(axis);
			double xPrime = x2 * dotProduct * (1.0D - cosTheta) + x * cosTheta + (-z2 * y + y2 * z) * sinTheta;
			double yPrime = y2 * dotProduct * (1.0D - cosTheta) + y * cosTheta + (z2 * x - x2 * z) * sinTheta;
			double zPrime = z2 * dotProduct * (1.0D - cosTheta) + z * cosTheta + (-y2 * x + x2 * y) * sinTheta;
			vector.setX(xPrime).setY(yPrime).setZ(zPrime);
			vector.add(point);
		}
		return this;
	}

	public abstract List<V> getVectors();
}
